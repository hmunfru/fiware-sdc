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

package com.telefonica.euro_iaas.sdc.util;

import java.text.MessageFormat;

import com.telefonica.euro_iaas.sdc.model.ProductInstance;


import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.BACKUP_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.CONFIGURE_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.DEPLOYAC_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.INSTALL_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.RESTORE_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNDEPLOYAC_PRODUCT_RECIPE_TEMPLATE;
import static com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider.UNINSTALL_PRODUCT_RECIPE_TEMPLATE;

/**
 * Default RecipeNamingGenerator implementation.
 * 
 * @author Sergio Arroyo
 */
public class RecipeNamingGeneratorImpl implements RecipeNamingGenerator {

    private SystemPropertiesProvider propertiesProvider;

    /**
     * {@inheritDoc}
     */
    public String getInstallRecipe(ProductInstance product) {
        String installTemplate = propertiesProvider.getProperty(INSTALL_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(installTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    public String getUninstallRecipe(ProductInstance product) {
        String uninstallTemplate = propertiesProvider.getProperty(UNINSTALL_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(uninstallTemplate, product);

    }

    /**
     * {@inheritDoc}
     */
    public String getDeployArtifactRecipe(ProductInstance productInstance) {
        String deployACTemplate = propertiesProvider.getProperty(DEPLOYAC_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(deployACTemplate, productInstance);
    }

    /**
     * {@inheritDoc}
     */
    public String getUnDeployArtifactRecipe(ProductInstance productInstance) {
        String unDeployACTemplate = propertiesProvider.getProperty(UNDEPLOYAC_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(unDeployACTemplate, productInstance);

    }

    /**
     * {@inheritDoc}
     */

    public String getBackupRecipe(ProductInstance product) {
        String backupTemplate = propertiesProvider.getProperty(BACKUP_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(backupTemplate, product);

    }

    public String getConfigureRecipe(ProductInstance product) {
        String configureTemplate = propertiesProvider.getProperty(CONFIGURE_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(configureTemplate, product);

    }

    /**
     * {@inheritDoc}
     */

    public String getRestoreRecipe(ProductInstance product) {
        String restoreTemplate = propertiesProvider.getProperty(RESTORE_PRODUCT_RECIPE_TEMPLATE);
        return populateProductRecipe(restoreTemplate, product);

    }

    /**
     * Fill the template with product name and product version.
     * 
     * @param template
     *            the template
     * @param product
     *            the product
     * @return the filled template
     */
    private String populateProductRecipe(String template, ProductInstance product) {
        return MessageFormat.format(template, product.getProductRelease().getProduct().getName(), product
                .getProductRelease().getVersion());
    }

    // ///////////I.O.C.////////////

    /**
     * @param propertiesProvider
     *            the propertiesProvider to set
     */
    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

}
