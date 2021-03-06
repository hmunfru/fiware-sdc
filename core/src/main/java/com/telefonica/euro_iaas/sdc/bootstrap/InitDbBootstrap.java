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

package com.telefonica.euro_iaas.sdc.bootstrap;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.NodeCommandDao;
import com.telefonica.euro_iaas.sdc.dao.OSDao;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.dao.ProductReleaseDao;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.NodeCommand;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;

/**
 * Populates data base with synthetic data to emulate the preconditions of SO management, Image deployable management
 * and customer management.
 * 
 * @author Sergio Arroyo
 * @version $Id: $
 */
// TODO delete this class when the preconditions are done.
public class InitDbBootstrap implements ServletContextListener {

    /** {@inheritDoc} */
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        OSDao soDao = (OSDao) ctx.getBean("osDao");
        ProductDao productDao = (ProductDao) ctx.getBean("productDao");
        ProductReleaseDao productReleaseDao = (ProductReleaseDao) ctx.getBean("productReleaseDao");
        NodeCommandDao nodeCommandDao = (NodeCommandDao) ctx.getBean("nodeCommandDao");

        try {
            soDao.load("95");
            soDao.load("94");
        } catch (EntityNotFoundException e) {
            try {

                // OSs
                OS so1 = new OS("94", "Ubuntu", "Ubuntu 10.04", "10.04");
                OS so2 = new OS("95", "Debian", "Debian 5", "5");
                OS so3 = new OS("76", "Centos", "Centos 2.9", "2.9");

                so1 = soDao.create(so1);
                so2 = soDao.create(so2);
                so3 = soDao.create(so3);
                List<OS> supportedSSOO1 = Arrays.asList(so1);
                List<OS> supportedSSOO2 = Arrays.asList(so2);
                List<OS> supportedSSOO3 = Arrays.asList(so3);
                List<OS> supportedSSOO12 = Arrays.asList(so1, so2);
                List<OS> supportedSSOO123 = Arrays.asList(so1, so2, so3);

                NodeCommand nodeCommandso11 = new NodeCommand(so1, "chefclient_command", "chef-client");
                nodeCommandso11 = nodeCommandDao.create(nodeCommandso11);
                NodeCommand nodeCommandso12 = new NodeCommand(so1, "hostname_command", "hostname");
                nodeCommandso12 = nodeCommandDao.create(nodeCommandso12);
                NodeCommand nodeCommandso13 = new NodeCommand(so1, "domain_command", "dnsdomainname");
                nodeCommandso13 = nodeCommandDao.create(nodeCommandso13);
                NodeCommand nodeCommandso14 = new NodeCommand(so1, "ipaddress_command", "hostname -I");
                nodeCommandso14 = nodeCommandDao.create(nodeCommandso14);
                NodeCommand nodeCommandso21 = new NodeCommand(so2, "chefclient_command", "chef-client");
                nodeCommandso21 = nodeCommandDao.create(nodeCommandso21);
                NodeCommand nodeCommandso22 = new NodeCommand(so2, "hostname_command", "hostname");
                nodeCommandso22 = nodeCommandDao.create(nodeCommandso22);
                NodeCommand nodeCommandso23 = new NodeCommand(so2, "domain_command", "dnsdomainname");
                nodeCommandso23 = nodeCommandDao.create(nodeCommandso23);
                NodeCommand nodeCommandso24 = new NodeCommand(so2, "ipaddress_command", "hostname -I");
                nodeCommandso24 = nodeCommandDao.create(nodeCommandso24);
                // CentOS
                NodeCommand nodeCommandso31 = new NodeCommand(so3, "chefclient_command", "chef-client");
                nodeCommandso31 = nodeCommandDao.create(nodeCommandso31);
                NodeCommand nodeCommandso32 = new NodeCommand(so3, "hostname_command", "hostname");
                nodeCommandso32 = nodeCommandDao.create(nodeCommandso32);
                NodeCommand nodeCommandso33 = new NodeCommand(so3, "domain_command", "dnsdomainname");
                nodeCommandso33 = nodeCommandDao.create(nodeCommandso33);
                NodeCommand nodeCommandso34 = new NodeCommand(so3, "ipaddress_command", "hostname -I");
                nodeCommandso34 = nodeCommandDao.create(nodeCommandso34);

                // Tomcat Product Releases
                Product tomcat = new Product("tomcat", "tomcat J2EE container");
                tomcat.addAttribute(new Attribute("port", "8080", "The listen port"));
                tomcat.addAttribute(new Attribute("ssl_port", "8443", "The ssl listen port"));
                tomcat.addAttribute(new Attribute("ssl_port", "8443", "The ssl listen port"));
                tomcat.addAttribute(new Attribute("id_web_server", "default", "The id web server"));

                tomcat.addAttribute(new Attribute("sdcgroupid", "id_web_server", "sdcgroupid"));

                tomcat.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                tomcat = productDao.create(tomcat);

                ProductRelease tomcat7 = new ProductRelease("6", "Tomcat server 6", tomcat, supportedSSOO123, null);
                tomcat7 = productReleaseDao.create(tomcat7);

                Product nodejs = new Product("nodejs", "nodejs");
                nodejs.addAttribute(new Attribute("aux", "aux", "aux"));
                nodejs.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                nodejs = productDao.create(nodejs);

                ProductRelease nodejsr = new ProductRelease("0.6.15", "Nodejs 0.6.15", nodejs, supportedSSOO123, null);
                nodejsr = productReleaseDao.create(nodejsr);

                Product mysql = new Product("mysql", "mysql");
                mysql.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                mysql = productDao.create(mysql);
                mysql.addAttribute(new Attribute("aux", "aux", "aux"));

                ProductRelease mysql124 = new ProductRelease("1.2.4", "mysql 1.2.4", mysql, supportedSSOO123, null);
                mysql124 = productReleaseDao.create(mysql124);

                Product git = new Product("git", "git");
                git.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                git = productDao.create(git);
                ProductRelease git17 = new ProductRelease("1.7", "git 1.7", git, supportedSSOO123, null);
                git17 = productReleaseDao.create(git17);

                Product mongoshard = new Product("mongodbshard", "mongodbshard");
                mongoshard.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                mongoshard = productDao.create(mongoshard);
                ProductRelease mongoshard223 = new ProductRelease("2.2.3", "mongodb shard 2.2.3", mongoshard,
                        supportedSSOO123, null);
                mongoshard223 = productReleaseDao.create(mongoshard223);

                Product mongos = new Product("mongos", "mongos");
                mongos.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                mongos = productDao.create(mongos);
                ProductRelease mongos223 = new ProductRelease("2.2.3", "mongos 2.2.3", mongos, supportedSSOO123, null);
                mongos223 = productReleaseDao.create(mongos223);

                Product mongodbconfig = new Product("mongodbconfig", "mongodbconfig");
                mongodbconfig.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                mongodbconfig = productDao.create(mongodbconfig);
                ProductRelease mongodbconfig223 = new ProductRelease("2.2.3", "mongodb shard 2.2.3", mongodbconfig,
                        supportedSSOO123, null);
                mongodbconfig223 = productReleaseDao.create(mongodbconfig223);

                Product contextbroker = new Product("contextbroker", "contextbroker");
                contextbroker.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                contextbroker = productDao.create(contextbroker);

                ProductRelease contextbroker100 = new ProductRelease("1.0.0", "contextbroker 1.0.0", contextbroker,
                        supportedSSOO123, null);
                contextbroker100 = productReleaseDao.create(contextbroker100);

                /*
                 * ProductRelease tomcat6 = new ProductRelease("6", "Tomcat server 6", null, tomcat, supportedSSOO123,
                 * null); tomcat6 = productReleaseDao.create(tomcat6); ProductRelease tomcat5 = new
                 * ProductRelease("5.5", "Tomcat server 5.5", null, tomcat, supportedSSOO123, Arrays.asList(tomcat6));
                 * tomcat5 = productReleaseDao.create(tomcat5);
                 */

                /*
                 * tomcat5.setTransitableReleases(Arrays.asList(tomcat6)); tomcat5 = productReleaseDao.update(tomcat5);
                 * tomcat6.setTransitableReleases(Arrays.asList(tomcat5, tomcat7)); tomcat6 =
                 * productReleaseDao.update(tomcat6);
                 */
                // tomcat7.setTransitableReleases(Arrays.asList(tomcat6));
                // tomcat6 = productReleaseDao.update(tomcat6);

                // Postgresql Product Releases
                Product postgresql = new Product("postgresql", "db manager");
                postgresql.addAttribute(new Attribute("username", "postgres", "The administrator usename"));
                postgresql.addAttribute(new Attribute("password", "postgres", "The administrator password"));
                postgresql.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));
                postgresql = productDao.create(postgresql);

                ProductRelease postgres84 = new ProductRelease("8.4", "postgresql 8.4", postgresql, supportedSSOO123,
                        null);
                postgres84 = productReleaseDao.create(postgres84);

                /*
                 * ProductRelease postgres83 = new ProductRelease("8.3", "postgresql 8.3", null, postgresql,
                 * supportedSSOO123, Arrays.asList(postgres84)); postgres83 = productReleaseDao.create(postgres83);
                 */

                // haproxy Product Releases
                Product haproxy = new Product("haproxy", "balancer");
                haproxy.addAttribute(new Attribute("sdccoregroupid", "app_server_role", "idcoregroup"));
                haproxy.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));

                haproxy = productDao.create(haproxy);

                ProductRelease haproxy10 = new ProductRelease("1.0", "haproxy 1.0", haproxy, supportedSSOO123, null);
                haproxy10 = productReleaseDao.create(haproxy10);

                /*
                 * ProductRelease haproxy09 = new ProductRelease("0.9", "haproxy 0.9", null, haproxy, supportedSSOO123,
                 * null); haproxy09 = productReleaseDao.create(haproxy09);
                 */

                // Test Product Release Chef
                Product test = new Product("test", "test");
                test.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));
                test = productDao.create(test);

                ProductRelease test01 = new ProductRelease("0.1", "blah blah blah", test, supportedSSOO123, null);
                test01 = productReleaseDao.create(test01);

                // Test Product Release Puppet
                Product testPuppet = new Product("testPuppet", "testPuppet");
                testPuppet.addMetadata(new Metadata("installator", "puppet", "Puppet Manifest required"));
                testPuppet = productDao.create(testPuppet);

                ProductRelease testPuppet01 = new ProductRelease("0.1", "blah blah blah", testPuppet, supportedSSOO123,
                        null);
                testPuppet01 = productReleaseDao.create(testPuppet01);

                // Wiki Product Releases
                Product mediawiki = new Product("mediawiki", "MediaWiki Product");
                mediawiki.addAttribute(new Attribute("wikiname", "Wiki to be shown", "The name of the wiki"));
                mediawiki.addAttribute(new Attribute("path", "/demo", "The url context to be displayed"));
                mediawiki.addMetadata(new Metadata("installator", "chef", "ChefServer Recipe required"));
                // mediawiki.addAttribute(new Attribute("logogif", "",
                // "The url context to be displayed"));
                mediawiki = productDao.create(mediawiki);

                ProductRelease mediawiki1 = new ProductRelease("1.17.0", "Mediawiki 1.17.0", mediawiki,
                        supportedSSOO123, null);
                mediawiki1 = productReleaseDao.create(mediawiki1);

            } catch (Exception e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
        }
    }

    /** {@inheritDoc} */

    public void contextDestroyed(ServletContextEvent event) {
    }
}
