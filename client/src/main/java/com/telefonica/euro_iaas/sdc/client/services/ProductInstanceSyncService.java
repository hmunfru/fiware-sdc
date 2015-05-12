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

package com.telefonica.euro_iaas.sdc.client.services;

import java.util.List;

import com.telefonica.euro_iaas.sdc.client.exception.InvalidExecutionException;
import com.telefonica.euro_iaas.sdc.client.exception.MaxTimeWaitingExceedException;
import com.telefonica.euro_iaas.sdc.client.exception.ResourceNotFoundException;
import com.telefonica.euro_iaas.sdc.model.InstallableInstance.Status;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;

/**
 * Provides the operations to work with application instances in a synchronous mode
 * 
 * @author Sergio Arroyo
 */

public interface ProductInstanceSyncService {
	
    /**
     * Install a product in a given host.
     * 
     * @param product
     *            the concrete release of a product to install. It also contains information about the VM where the
     *            product is going to be installed
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the installed product.
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    ProductInstanceDto install(String vdc, ProductInstanceDto product, String token) throws MaxTimeWaitingExceedException,
            InvalidExecutionException;

    /**
     * Retrieve all ProductInstance created in the system.
     * 
     * @param hostname
     *            the host name where the product is installed (<i>nullable</i>)
     * @param domain
     *            the domain where the machine is (<i>nullable</i>)
     * @param ip
     *            the ip of the host (<i>nullable</i>)
     * @param fqn
     *            the fqn of the host (<i>nullable</i>)
     * @param page
     *            for pagination is 0 based number(<i>nullable</i>)
     * @param pageSize
     *            for pagination, the number of items retrieved in a query (<i>nullable</i>)
     * @param orderBy
     *            the file to order the search (id by default <i>nullable</i>)
     * @param orderType
     *            defines if the order is ascending or descending (asc by default <i>nullable</i>)
     * @param status
     *            the status the product (<i>nullable</i>)
     * @param vdc
     *            defines the vdc where the products are installed (<i>not nullable</i>).
     * @return the product instances that match with the criteria.
     */
    List<ProductInstanceDto> findAll(String hostname, String domain, String ip, String fqn, Integer page,
            Integer pageSize, String orderBy, String orderType, Status status, String vdc, String productName, String token);
    
    /**
     * Retrieve the selected application instance.
     * 
     * @param vdc
     *            the vdc
     * @param id
     *            the application id
     * @return the installable instance.
     * @throws ResourceNotFoundException
     *             if the resource does not found
     */
    ProductInstanceDto load(String vdc, String name, String token) throws ResourceNotFoundException;

    /**
     * Retrieve the selected application instance.
     * 
     * @param url
     *            the url where the installable isntance is
     * @return the installable instance.
     * @throws ResourceNotFoundException
     *             if the resource does not found
     */
    ProductInstanceDto loadUrl(String url, String token, String tenant) throws ResourceNotFoundException;
    
    /**
     * Upgrade the selected instance version.
     * 
     * @param id
     *            the installable instance id
     * @param new version the new version to upgrade to async operation will be sent
     * @return the task
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
     ProductInstanceDto upgrade(String vdc, String name, String version, String token) throws MaxTimeWaitingExceedException, InvalidExecutionException;

    /**
     * Configure the selected instance.
     * 
     * @param id
     *            the installable instance id
     * @param arguments
     *            the configuration properties async operation will be sent
     * @return the task.
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
     ProductInstanceDto configure(String vdc, String name, List<Attribute> arguments,String token) throws MaxTimeWaitingExceedException,
            InvalidExecutionException;

    /**
     * Uninstall a previously installed instance.
     * 
     * @param id
     *            the installable instance id
     * @param callback
     *            if not empty, contains the url where the result of the async operation will be sent
     * @return the task.
     * @throws MaxTimeWaitingExceedException
     *             if the operation spend more time that is allowed
     * @throws InvalidExecutionException
     *             if the return of the task is not success
     */
    // T uninstall(String vdc, Long id)
    // throws MaxTimeWaitingExceedException, InvalidExecutionException;

     ProductInstanceDto uninstall(String vdc, String name, String token) throws MaxTimeWaitingExceedException, InvalidExecutionException;


}
