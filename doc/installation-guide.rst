SDC Build and Installation
==========================


Requirements
============

In order to execute the SDC, it is needed to have previously installed
the following software:

-  Chef node
-  Chef server
   [http://wiki.opscode.com/display/chef/Installing+Chef+Server\ ]. For
   CentOS it is possible to follow the following
   instructions[http://blog.frameos.org/2011/05/19/installing-chef-server-0-10-in-rhel-6-scientificlinux-6/\ ]

-  SDC node
-  open jdk 7
-  PostgreSQL [http://www.postgresql.org/\ ]

SDC should be installed in a host with 2Gb RAM.

Installation  (for CentOS)
==========================

Install SDC from RPM
--------------------
  
The SDC is packaged as RPM and stored in the rpm repository. Thus, the first thing to do is to create a file 
in /etc/yum.repos.d/fiware.repo, with the following content.

 .. code::
 
	[Fiware]
	name=FIWARE repository
	baseurl=http://repositories.testbed.fi-ware.eu/repo/rpm/x86_64/
	gpgcheck=0
	enabled=1
    
After that, you can install the SDC just doing:

.. code::

	yum install fiware-sdc

or specifying the version

.. code::

	yum install fiware-sdc-{version}-1.noarch

to install a specific SDC version where {version} could be "2.6.0"


Install SDC from source
-----------------------

Requirements: To install SDC from source it is required to have the following software installed in your host
previously:

- git

- java 1.7

- maven

Here we include a small guide to install the required software. If you find any problem in the installation process,
please refer to the official sites:

Install git

.. code::

   sudo yum install git

Install java 1.7

.. code::

   sudo yum install java-1.7.0-openjdk-devel

Install maven 2.5

.. code::

	sudo yum install wget
	wget http://mirrors.gigenet.com/apache/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz
	su -c "tar -zxvf apache-maven-3.2.5-bin.tar.gz -C /usr/local"
	cd /usr/local
	sudo ln -s apache-maven-3.2.5 maven

Add the following lines to the file /etc/profile.d/maven.sh

.. code::

	# Add the following lines to maven.sh
	export M2_HOME=/usr/local/maven
	export M2=$M2_HOME/bin
	PATH=$M2:$PATH

In order to check that your maven installation is OK, you shluld exit your current session with "exit" command, enter again
and type

.. code::

	mvn -version

if the system shows the current maven version installed in your host, you are ready to continue with this guide.

Now we are ready to build the SDC rpm and finally install it

The SDC is a maven application so, we should follow following instructions:

- Download SDC code from github

.. code::

   git clone -b develop https://github.com/telefonicaid/fiware-sdc

- Go to fiware-sdc folder and compile, launch test and build all modules

.. code::
	
    cd fiware-sdc/
    mvn clean install

-  Create a zip with distribution in target/sdc-server-dist.zip

.. code ::

       $ mvn assembly:assembly -DskipTests
       
       #$ cp target/distribution/sdc-server-dist {folder}
       #$ {folder}/sdc-server-dist/bin/generateselfsigned.sh start 
       #$ cd {folder}/sdc-server-dist/bin ; ./jetty.sh start 

-  You can generate a rpm o debian packages (using profiles in pom)

for debian/ubuntu:

.. code::

       $ mvn install -Pdebian -DskipTests
       (created target/sdc-server-XXXXX.deb)

for centOS (you need to have installed rpm-bluid. If not, please type "yum install rpm-build" ):
   
.. code::

   		$ mvn package -P rpm -DskipTests
   		(created target/rpm/sdc/RPMS/noarch/fiware-sdc-XXXX.noarch.rpm)

Finally go to the folder where the rpm has been created (target/rpm/sdc/RPMS/noarch) and execute

.. code::

	cd target/rpm/fiware-sdc/RPMS/noarch
	rpm -i <rpm-name>.rpm

Please, be aware  that the supported installation method is the RPM package. If you use other method, some extra steps may be required. For example you would need to generate manually the certificate (See the section about "Configuring the HTTPS certificate" for more information):

.. code::

   fiware-sdc/bin/generateselfsigned.sh
   
Requirements: Installation instructions
---------------------------------------

Chef server
~~~~~~~~~~~

Chef server Installation (Centos 6.5)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The installation of the chef-server involves to install the chef-server
package, which can be obtained in [http://www.getchef.com/chef/install/\ ]. If you find any problem in the chef-server
installation process, please refer to the chef-serve official site. This small guide has been tested on Centos6.5

Go to this url and select the chef-server version you are interested in depending also on you operating system.
Copy the url to download the chef-server selected version and type

.. code::

	wget <chef-server-url>

in this example we have 

.. code::

	chef-server-url = https://opscode-omnibus-packages.s3.amazonaws.com/el/6/x86_64/chef-server-11.1.6-1.el6.x86_64.rpm

In case you do not have wget installed on your syste, please type yum install wget to install it. We can just execute

.. code::

    mv chef-server-11.1.6-1.el6.x86_64.rpm chef-server-package.rpm
    rpm -Uvh chef-server-package.rpm

Verify the the hostname for the Chef server by running the hostname
command. The hostname for the Chef server must be a FQDN. This means
hostaname.domainame. In case it is not configure, you can do it

.. code::

    hostname chef-server.localdomain

and include it in the /etc/hosts

After that, it is required to configure the certificates and other
staff in the chef-server, with chef-server-ctl. This command will set up
all of the required components, including Erchef, RabbitMQ, and
PostgreSQL.

.. code::

    sudo chef-server-ctl reconfigure

In order to test verify the installation of Chef Server 11.x by running
the following command:

.. code::

    sudo chef-server-ctl test

After that, you can obtain the different certificates for the
different clients in /etc/chef-server. There you can find a
chef-validator.pem (needed for all the nodes), the chef-server-gui for
the GUI. You can copy them in order to use them later.

Chef server cookbook repository
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The FI-WARE cookbook repository is in FI-WARE SVN repository. To upload
the recipes into the chef server you need:

-  To dowload the svn repository (yum install svn if not installed):

.. code::

   svn checkout https://forge.fiware.org/scmrepos/svn/testbed/trunk/cookbooks

-  Inside the cookbooks folder, create a file update with the following
   content. It will update the repository and upload into the chef-server

.. code::

    svn update
    knife cookbook upload --all -o BaseRecipes/
    knife cookbook upload --all -o BaseSoftware/
    knife cookbook upload --all -o GESoftware/

Chef-client installation and configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The next step is to configure a client in the chef-server so that you
can execute the chef-server CLI. To do that, you need to install the
chef-client

.. code::

    curl -L https://www.opscode.com/chef/install.sh | sudo bash

Before you configure the chef-client you should add the admin-pem and chef-validator.pem
to the directory where chef-client finds its configuration (By default shoud be $HOME/.chef),
In this directory should be placed the admin.pem and chef-validator.pem files before you start
with the chef-client configuration.

To configure chef-client, type the following command. You can accept all the
default

.. code::

    knife configure --initial

The script will ask the following parameters:

- Please enter the chef server URL: use the FQDN for the Chef server

- A name for the new user: use "station1"

- A name for the admin user [admin]: keep the default option

- location of the existing admin's private key: type the new location given

- the validation clientname: [chef-validator]: keep the default option

- location of the validation key: [/etc/chef-server/chef-validator.pem]: type the new location given

- the path to a chef repository (or leave blank): type the location chosen in the previous section

-  password for the new user: type the password you have in mind

It is possible that the first time you got an error due to the autosigned-certificate of the chef-server. If this is
the case, pleawe follow the instructions you have in the screen and type knife ssl fetch to accept this certificate.

Once you have a client configured, you can run the CLI. Just one
example:

.. code::

    knife client list

.. code::
     
     knife user list

Puppet
~~~~~~

To install Puppet component, pleae refer to the following Puppet Installation Guide at 
[https://github.com/telefonicaid/fiware-puppetwrapper/blob/develop/doc/installation-guide.rst]

Requirements: Install PostgreSQL
--------------------------------

The SDC node needs to have PostgreSQL installed in service mode and a
database created called SDC. For CentOS, these are the instructions:

Firstly, it is required to install the PostgreSQL
[http://wiki.postgresql.org/wiki/YUM_Installation\ ].

.. code:: 

     yum install postgresql postgresql-server postgresql-contrib


Start Postgresql
~~~~~~~~~~~~~~~~

Type the following commands to install the postgresql as service and
restarted

.. code::

    chkconfig --add postgresql
    chkconfig postgresql on
    service postgresql initdb
    service postgresql start

Then, you need to configure postgresql to allow for accessing. In
/var/lib/pgsql/data/postgresql.conf

.. code::

    listen_addresses = '0.0.0.0'

We need to create the sdc database. To do that we need to connect as postgres user to the PostgreSQL
server and set the password for user postgres using alter user as below:

.. code::

    su - postgres
    postgres$ psql postgres postgres;
    psql (8.4.13)
    Type "help" for help.
    postgres=# alter user postgres with password 'postgres';
    postgres=# create database sdc;
    postgres=# grant all privileges on database sdc to postgres;
    postgres=#\q
    exit
    
In /var/lib/pgsql/data/pg\_hba.conf, change the table at the end of the file to
look like:

.. code::

    #TYPE   DATABASE  USER        CIDR-ADDRESS          METHOD
    #"local" is for Unix domain socket connections only
    local   all       all                               ident
    # IPv4 local connections:
    host    all       all         127.0.0.1/32          md5
    # IPv6 local connections:
    host    all       all         ::1/128               md5


Restart the postgres 

.. code::

     service postgresql restart


Check that the database has been created correctly:

.. code::

   $ su - postgres
   postgres$ cd /opt/fiware/sdc-/resources
   $ psql postgres postgres
   postgres=#\c sdc
   postgres=# \i db-initial.sql
   postgres=# \i db-changelog.sql
   exit
   

Then we need to create the database tables for the sdc. To do that
obtain the files from
[https://github.com/telefonicaid/fiware-sdc/blob/develop/migrations/src/main/resources\ ]
and execute

.. code::

   $ psql -d sdc -a -f db-initial.sql
   $ psql -d sdc -a -f db-changelog.sql


Configure SDC application
^^^^^^^^^^^^^^^^^^^^^^^^^

Once the prerequisites are satisfied, you change the context file. To do
that, change sdc.xml found in distribution file and store it in folder
$SDC\_HOME/webapps/.

See the snipet bellow to know how it works:

.. code::

    <New id="sdc" class="org.eclipse.jetty.plus.jndi.Resource">
        <Arg>jdbc/sdc</Arg>
        <Arg>

            <New class="org.postgresql.ds.PGSimpleDataSource">
                <Set name="User"> <database user> </Set>
                <Set name="Password"> <database password> </Set>
                <Set name="DatabaseName"> <database name>   </Set>
                <Set name="ServerName"> <IP/hostname> </Set>
                <Set name="PortNumber">5432</Set>
            </New>

        </Arg>
    </New>

Configuring the SDC as service 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Once we have installed and configured the paas manager, the next step is to configure it as a service. To do that just create a file in 
/etc/init.d/fiware-sdc with the following content

.. code::

    #!/bin/bash
    # chkconfig: 2345 20 80
    # description: Description comes here....
    # Source function library.
    . /etc/init.d/functions
    start() {
        /opt/fiware-sdc/bin/jetty.sh start
    }
    stop() {
        /opt/fiware-sdc/bin/jetty.sh stop
    }
    case "$1" in 
        start)
            start
        ;;
        stop)
            stop
        ;;
        restart)
            stop
            start
        ;;
        status)
            /opt/fiware-sdc/bin/jetty.sh status
        ;;
        *)
            echo "Usage: $0 {start|stop|status|restart}"
    esac
    exit 0 

Now you need to execute:

.. code::

    chkconfig --add fiware-sdc
    chkconfig fiware-sdc on
    service fiware-sdc start
    


The configuration of SDC is in configuration\_properties table. There,
it is required to configure:

-  openstack-tcloud.keystone.url: This is the url where the keystone-proxy is deployed
-  openstack-tcloud.keystone.user: the admmin user
-  openstack-tcloud.keystone.password: the admin password
-  openstack-tcloud.keystone.tenant: the admin tenant
-  sdc\_manager\_url: the final url, mainly http://sdc-ip:8080/sdc

The updates of the columns are done in the following way

.. code::

 	su - potgres
    postgres$ psql -U postgres -d sdc
    Password for user postgres: <postgres-password-previously-chosen>
    postgres=# UPDATE configuration_properties SET value='<the value>' where key='sdc_manager_url';
    postgres=# UPDATE configuration_properties SET value='<the value>' where key='openstack-tcloud.keystone.user';
    postgres=# UPDATE configuration_properties SET value='<the value>' where key='openstack-tcloud.keystone.pass';
    postgres=# UPDATE configuration_properties SET value='<the value>' where key='openstack-tcloud.keystone.tenant';
    postgres=# UPDATE configuration_properties SET value='<the value>' where key='openstack-tcloud.keystone.url';

The last step is to create a sdc client in the chef-server, so that, the
SDC can communicate with the chef-server. To do that, we can use the
chef-server-web-ui, which is usually deployed on https://chef-server-ip,
go to https://chef-server-ip/clients and create a sdc client as
administrator. Then, it is required to copy the private key.

In the sdc machine, it is required to copy this private key in
/etc/chef/sdc.pem (you can configure the path also in the properties)

Register SDC application into keystone
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The last step involves to regiter the SDC, chef-server, puppetwrapper and puppetmaster endpoints into
the keystone endpoint catalogue. To do that, you should write into the
config.js in the keystone-proxy the following lines:

.. code::

     {"endpoints": [
        {"adminURL": "http://sdc-ip:8080/sdc/rest",
        "region": "myregion",
        "internalURL": "http://sdc-ip:8080/sdc/rest",
        "publicURL": "http://sdc-ip:8080/sdc/rest"
        }
        ],
        "endpoints_links": [],
        "type": "sdc",
        "name": "sdc"
    },
    {"endpoints": [
        {"adminURL": "https://chef-server-ip",
        "region": "myregion",
        "internalURL": "https://chef-server-ip",
        "publicURL": "https://chef-server-ip"
        }
        ],
        "endpoints_links": [],
        "type": "chef-server",
        "name": "chef-server"
    },
    {"endpoints": [
        {"adminURL": "https://130.206.81.91:8443/puppetwrapper/"
         "region": "myregion"
         "internalURL": "https://130.206.81.91:8443/puppetwrapper/"
         "publicURL": "https://130.206.81.91:8443/puppetwrapper/"
        }
        ],
        "endpoints_links": [],
        "type": "puppetwrapper",
        "name": "puppetwrapper"
    },
    {"endpoints": [
        {"adminURL": "puppet-master.dev-havana.fi-ware.org"
         "region": "myregion"
         "internalURL": "puppet-master.dev-havana.fi-ware.org"
         "publicURL": "puppet-master.dev-havana.fi-ware.org"
        }
        ],
        "endpoints_links": [],
        "type": "puppetmaster",
        "name": "puppetmaster"
    },

where myregion should be the name of the openstack region defined.

Creating images sdc-aware
-------------------------

The images to be deployed by the SDC, should have some features, like to
have the chef-client installed and configured correctly with the
chef-server. In the roadmap, it is considered to avoid all this process
and to make possible any image to be SDC-aware, installing and
configuring everything in booting status.

.. code::

    mkdir /etc/chef
    mkdir /var/log/chef
    curl -L https://www.opscode.com/chef/install.sh | bash

You should copy the chef-validator.pem from the chef-server into
/etc/chef

Then, it is required to create a file called client.rb in /etc/chef. The
validation.pem should be obtained from the chef-server in the folder
/etc/chef-server and its called chef-validator.pem and rename to
validation.pem in the /etc/chef folder of the image

.. code::

    log_location           "/var/log/chef/client.log"
    ssl_verify_mode        :verify_none
    validation_client_name "chef-validator"
    validation_key         "/etc/chef/validation.pem"
    client_key             "/etc/chef/client.pem"
    chef_server_url        "https://cher-server-ip"

Finally, to start chef-client in boot time

.. code::

    chef-client -i 60 -s 6

.. |Build Status| image:: https://travis-ci.org/telefonicaid/fiware-sdc.svg
   :target: https://travis-ci.org/telefonicaid/fiware-sdc
.. |Coverage Status| image:: https://coveralls.io/repos/telefonicaid/fiware-sdc/badge.png?branch=develop
   :target: https://coveralls.io/r/telefonicaid/fiware-sdc
.. |help stackoverflow| image:: http://b.repl.ca/v1/help-stackoverflow-orange.png
   :target: http://www.stackoverflow.com

Configuring the HTTPS certificate
---------------------------------

The service is configured to use HTTPS to secure the communication between clients and the server. One central point 
in HTTPS security is the certificate which guarantee the server identity.

Quickest solution: using a self-signed certificate
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The service works "out of the box" against passive attacks (e.g. a sniffer) because a self-signed certificated is 
generated automatically when the RPM is installed. Any certificate includes a special field call "CN" (Common name) 
with the identity of the host: the generated certificate uses as identity the IP of the host.

The IP used in the certificate should be the public IP (i.e. the floating IP). The script which generates the 
certificate knows the public IP asking to an Internet service (http://ifconfig.me/ip). Usually this obtains the 
floating IP of the server, but of course it wont work without a direct connection to Internet.

If you need to regenerate a self-signed certificate with a different IP address (or better, a convenient configured 
hostname), please run:

.. code::

    /opt/fiware-sdc/bin/generateselfsigned.sh myhost.mydomain.org

By the way, the self-signed certificate is at /etc/keystorejetty. This file wont be overwritten although you reinstall 
the package. The same way, it wont be removed automatically if you uninstall de package.

Advanced solution: using certificates signed by a CA
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Although a self-signed certificate works against passive attack, it is not enough by itself to prevent active attacks, 
specifically a "man in the middle attack" where an attacker try to impersonate the server. Indeed, any browser warns 
user against self-signed certificates. To avoid these problems, a certificate conveniently signed by a CA may be used.

If you need a certificate signed by a CA, the more cost effective and less intrusive practice when an organization has 
several services is to use a wildcard certificate, that is, a common certificate among all the servers of a DNS domain. 
Instead of using an IP or hostname in the CN, an expression as ".fiware.org" is used.

This solution implies:

* The service must have a DNS name in the domain specified in the wildcard certificate. For example, if the domain is ".fiware.org" a valid name may be "sdc.fiware.org".

* The clients should use this hostname instead of the IP

* The file /etc/keystorejetty must be replaced with another one generated from the wildcard certificate, the corresponding private key and other certificates signing the wild certificate.

It's possible that you already have a wild certificate securing your portal, but Apache server uses a different file format. 
A tool is provided to import a wildcard certificate, a private key and a chain of certificates, into /etc/keystorejetty:

.. code::

    # usually, on an Apache installation, the certificate files are at /etc/ssl/private
    /opt/fiware-sdc/bin/importcert.sh key.pem cert.crt chain.crt

If you have a different configuration, for example your organization has got its own PKI, please refer 
to: http://docs.codehaus.org/display/JETTY/How%2bto%2bconfigure%2bSSL

Sanity check procedures
=======================

Sanity check procedures
-----------------------
The Sanity Check Procedures are the steps that a System Administrator will take to verify that an installation is ready to be tested. This is therefore a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.

End to End testing
------------------
Although one End to End testing must be associated to the Integration Test, we can show here a quick testing to check that everything is up and running. It involves to obtain the product information storaged in the catalogue. With it, we test that the service is running and the database configure correctly.

.. code ::

    https://{SDC\_IP}:{port}/sdc/rest

The request to test it in the testbed should be

 .. code::

     curl -v -k -H 'Access-Control-Request-Method: GET' -H 'Content-Type: application xml' -H 'Accept: application/xml' -H 'X-Auth-Token: 5d035c3a29be41e0b7007383bdbbec57' -H 'Tenant-Id: 60b4125450fc4a109f50357894ba2e28' -X GET 'https://localhost:8443/sdc/rest/catalog/product'

the option -k should be included in the case you have not changed the security configuration of SDC. The result should be the product catalog.

If you obtain a 401 as a response, please check the admin credentials and the connectivity from the sdc machine to the keystone (openstack-tcloud.keystone.url in configuration_properties table)


List of Running Processes
-------------------------
Due to the SDC basically is running over jetty, the list of processes must be only the Jetty and PostgreSQL. If we execute the following command:

.. code::

     ps -ewF | grep 'postgres\|jetty' | grep -v grep

It should show something similar to the following:

  .. code::

   postgres  2396     1  0 58141  9228   0 11:51 ?        00:00:00 /usr/bin/postgres -D /var/lib/pgsql/data -p 5432
   postgres  2397  2396  0 47554  1224   0 11:51 ?        00:00:00 postgres: logger process
   postgres  2399  2396  0 58167  4400   0 11:51 ?        00:00:00 postgres: checkpointer process
   postgres  2400  2396  0 58141  1652   0 11:51 ?        00:00:00 postgres: writer process
   postgres  2401  2396  0 58141  1416   0 11:51 ?        00:00:00 postgres: wal writer process
   postgres  2402  2396  0 58349  2944   0 11:51 ?        00:00:00 postgres: autovacuum launcher process
   postgres  2403  2396  0 48110  1720   0 11:51 ?        00:00:00 postgres: stats collector process
   root      2859     1  0 599252 884004 0 11:59 ?        00:00:29 java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8585 -Dspring.profiles.active=fiware -Xmx1024m -Xms1024m -Djetty.state=/opt/fiware-sdc/jetty.state -Djetty.logs=/opt/fiware-sdc/logs -Djetty.home=/opt/fiware-sdc -Djetty.base=/opt/fiware-sdc -Djava.io.tmpdir=/tmp -jar /opt/fiware-sdc/start.jar jetty-logging.xml jetty-started.xml


Network interfaces Up & Open
----------------------------
Taking into account the results of the ps commands in the previous section, we take the PID in order to know the information about the network interfaces up & open. To check the ports in use and listening, execute the command:
  
.. code::

    netstat -p -a | grep $PID

Where $PID is the PID of Java process obtained at the ps command described before, in the previous case 2396 jetty and 2859 (postgresql). 
The expected results for the postgres process must be something like this output:

.. code::

    tcp        0      0 0.0.0.0:postgres        0.0.0.0:*               LISTEN      2396/postgres
    udp6       0      0 localhost:59289         localhost:59289         ESTABLISHED 2396/postgres
    unix  2      [ ACC ]     STREAM     LISTENING     35218    2396/postgres        /var/run/postgresql/.s.PGSQL.5432
    unix  2      [ ACC ]     STREAM     LISTENING     35220    2396/postgres        /tmp/.s.PGSQL.5432

and the following output for the jetty process:

.. code::

     tcp        0      0 0.0.0.0:8585            0.0.0.0:*               LISTEN      2859/java
     tcp6       0      0 [::]:pcsync-https       [::]:*                  LISTEN      2859/java
     unix  2      [ ]         STREAM     CONNECTED     48445    2859/java
     unix  2      [ ]         STREAM     CONNECTED     62299    2859/java
     unix  3      [ ]         STREAM     CONNECTED     48380    2859/java

Databases
---------
The last step in the sanity check, once that we have identified the processes and ports is to check the different databases that have to be up and accept queries. Fort he first one, if we execute the following commands:

.. code::

    psql -U postgres -d sdc

For obtaining the tables in the database, just use

.. code::

    sdc=# \dt

                     List of relations
      Schema |             Name              | Type  |  Owner
     --------+-------------------------------+-------+----------
      public | artifact                      | table | postgres
      public | artifact_attribute            | table | postgres
      public | attribute                     | table | postgres
      public | configuration_properties      | table | postgres
      public | installableinstance           | table | postgres
      public | installableinstance_attribute | table | postgres
      public | installablerelease            | table | postgres
      public | metadata                      | table | postgres
      public | nodecommand                   | table | postgres
      public | os                            | table | postgres
      public | product                       | table | postgres
      public | product_attribute             | table | postgres
      public | product_metadata              | table | postgres
      public | productinstance               | table | postgres
      public | productrelease                | table | postgres
      public | productrelease_os             | table | postgres
      public | productrelease_productrelease | table | postgres
      public | task                          | table | postgres
     
     (18 rows)

PupperWrapper
-------------

This is the PuppetWrapper API

PuppetWrapper API v2

.. code::
     
     ##POST /puppetwrapper/v2/node/{nodeName}/install ##json payload: 
     ##{"attributes":[{"value":"valor","key":"clave","id":23119,"description":null}],"version":"0.1","group":"alberts","softwareName":"testPuppet"} 

and example of curl could be:

.. code ::
 
     
.. code::
     
     ##POST /puppetwrapper/v2/node/{nodeName}/uninstall ##json payload: 
     ##{"attributes":[{"value":"valor","key":"clave","id":23119,"description":null}],"version":"0.1","group":"alberts","softwareName":"testPuppet"} 

.. code::

     ##GET /puppetwrapper/v2/node/{nodeName}/generate ##will generate the following files in /etc/puppet/manifests 
     ##add an import line to site.pp 
     ##generate the corresponding .pp file as group/nodeName.pp 

.. code::

     ##POST /puppetwrapper/module/{moduleName}/download ##payload : json as: {"url":”value”, ”repoSource”:”value”} 
     ##Value on repoSource can be: git /svn 
     ##will download the source code from the given url under {moduleName} directory. 

.. code::
     
     ##DELETE /puppetwrapper/v2/node/{nodeName} ##will delete the node: nodeName 

.. code::
     
     ##DELETE /puppetwrapper/v2/module/{modulename} ##will delete the module: moduleName 



