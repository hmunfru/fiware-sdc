/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.installator.impl;

import java.util.Date;
import java.util.List;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.exception.ShellCommandException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.IpToVM;

public class InstallatorChefImpl extends BaseInstallableInstanceManagerChef implements Installator {
    
    private IpToVM ip2vm;

    @Override
    public void callService(VM vm, String vdc, ProductRelease productRelease, String action)
            throws InstallatorException {
        // TODO Auto-generated method stub
        
    }
   
    @Override
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action)
            throws InstallatorException, NodeExecutionException {

        String process = productInstance.getProductRelease().getProduct().getName();

        String recipe = "";
        
        if ("install".equals(action)) {
            recipe = recipeNamingGenerator.getInstallRecipe(productInstance);
        } else if ("uninstall".equals(action)) {
            recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        }else if ("configure".equals(action)){
            recipe = recipeNamingGenerator.getConfigureRecipe(productInstance);
        }else if ("deployArtifact".equals(action)){
            recipe = recipeNamingGenerator.getDeployArtifactRecipe(productInstance);
        }else if ("undeployArtifact".equals(action)){
            recipe = recipeNamingGenerator.getUnDeployArtifactRecipe(productInstance);
        }else{
            throw new InstallatorException("Missing Action");
        }
        System.out.println("recipe " + recipe);
        
        configureNode(vm, attributes, process, recipe);
        try {
            LOGGER.info("Updating node with recipe " + recipe + " in " + vm.getIp());
            if (isSdcClientInstalled()) {
                executeRecipes(vm);
                // unassignRecipes(vm, recipe);
            } else {
                isRecipeExecuted(vm, process, recipe);
               // unassignRecipes(vm, recipe);
            }
        } catch (NodeExecutionException e) {
            // unassignRecipes(vm, recipe);
            // even if execution fails want to unassign the recipe
            throw new NodeExecutionException(e.getMessage());
        }
    }

    @Override
    public void callService(ProductInstance productInstance, String action) throws InstallatorException,
            NodeExecutionException {
        VM vm = productInstance.getVm();
        String recipe="";
        if ("uninstall".equals(action)) {
           recipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
        }else{
           
        }

        assignRecipes(vm, recipe);
        try {
            executeRecipes(vm);
            // unassignRecipes(vm, recipe);
        } catch (NodeExecutionException e) {
            // unassignRecipes(vm, recipe);
            // even if execution fails want to unassign the recipe
            throw new NodeExecutionException(e.getMessage());
        }
    }

    @Override
    public void upgrade(ProductInstance productInstance, VM vm) throws InstallatorException {
        try {
            String backupRecipe = recipeNamingGenerator.getBackupRecipe(productInstance);
            callChef(backupRecipe, vm);

            String uninstallRecipe = recipeNamingGenerator.getUninstallRecipe(productInstance);
            callChef(uninstallRecipe, vm);

            String installRecipe = recipeNamingGenerator.getInstallRecipe(productInstance);
            callChef(installRecipe, vm);

            String restoreRecipe = recipeNamingGenerator.getRestoreRecipe(productInstance);
            callChef(restoreRecipe, vm);
        } catch (NodeExecutionException e) {
            throw new InstallatorException(e);
        } catch (InstallatorException ex) {
            throw new InstallatorException(ex);
        }
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws ShellCommandException
     */
    public void executeRecipes(VM vm) throws NodeExecutionException {
        // tell Chef the assigned recipes shall be installed:
        sdcClientUtils.execute(vm);
    }

    /**
     * Add override attributes for the configured values.
     * 
     * @param vm
     *            the chef node
     * @param attributes
     *            the new attributes
     * @param recipe
     *            the recipe for that new attributes
     * @throws InstallatorException
     */
    public void configureNode(VM vm, List<Attribute> attributes, String process, String recipe)
            throws InstallatorException {
        // tell Chef the assigned recipes shall be deleted:
        // ChefNode node = chefNodeDao.loadNode(vm.getChefClientName());
        ChefNode node = null;
        try {
            node = chefNodeDao.loadNodeFromHostname(vm.getHostname());
            node.addRecipe(recipe);
            if (attributes != null) {
                for (Attribute attr : attributes) {
                    node.addAttribute(process, attr.getKey(), attr.getValue());
                }
            }
        } catch (EntityNotFoundException e) {
            String message = " Node with hostname " + vm.getHostname() + " is not registered in Chef Server";
            LOGGER.info(message);
            throw new InstallatorException(message, e);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
        try {
            chefNodeDao.updateNode(node);
        } catch (CanNotCallChefException e) {
            throw new InstallatorException(e);
        }
    }

    /**
     * Tell Chef the previously assigned recipes are ready to be installed.
     * 
     * @param osInstance
     * @throws
     * @throws ShellCommandException
     */
    public void isRecipeExecuted(VM vm, String process, String recipe) throws NodeExecutionException {
        boolean isExecuted = false;
        int time = 10000;
        Date fechaAhora = new Date();
        while (!isExecuted) {
            System.out.println("MAX_TIME: " + MAX_TIME + " and time: " + time);
            try {
                if (time > MAX_TIME) {
                    String errorMesg = "Recipe " + recipe + " coub not be executed in " + vm.getChefClientName();
                    LOGGER.info(errorMesg);
                    throw new NodeExecutionException(errorMesg);
                }
               
                Thread.sleep(time);
               
                ChefNode node = chefNodeDao.loadNodeFromHostname(vm.getHostname());

                long last_recipeexecution_timestamp = ((Double) node.getAutomaticAttributes().get("ohai_time"))
                        .longValue() * 1000;
                // Comprobar si el node tiene el recipe y sino vuelta a hacer la
                // peticion

                if (last_recipeexecution_timestamp > fechaAhora.getTime()) {
                    isExecuted = true;
                }
                time += time;
            } catch (EntityNotFoundException e) {
                throw new NodeExecutionException(e);
            } catch (CanNotCallChefException e) {
                throw new NodeExecutionException(e);
            } catch (InterruptedException ie) {
                throw new NodeExecutionException(ie);
            }
        }
    }

    @Override
    public void validateInstalatorData(VM vm) throws InvalidInstallProductRequestException, NodeExecutionException {
        if (isSdcClientInstalled()){
            if (!vm.canWorkWithChef()) {
                sdcClientUtils.checkIfSdcNodeIsReady(vm.getIp());
                sdcClientUtils.setNodeCommands(vm);

                vm = ip2vm.getVm(vm.getIp(), vm.getFqn(), vm.getOsType());
                // Configure the node with the corresponding node commands
            }
        } else {       
            if (!vm.canWorkWithInstallatorServer()) {
                String message = "The VM does not include the node hostname required to Install " +
                                "software";
                throw new InvalidInstallProductRequestException(message);
            }
            isNodeRegistered(vm.getHostname());
       }
    }

    /**
     * @param ip2vm
     *            the ip2vm to set
     */
    public void setIp2vm(IpToVM ip2vm) {
        this.ip2vm = ip2vm;
    }
    

}
