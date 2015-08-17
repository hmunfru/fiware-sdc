====================
FIWARE SDC | Saggita
====================

| |Build Status| |Coverage Status| |help stackoverflow|

.. contents:: :local:

Introduction
============
This is the code repository for FIWARE Saggita, the reference implementation
of the Software Deployment and Configuration GE.

This project is part of FIWARE_. Check also the
`FIWARE Catalogue - Software Deployment and Configuration GE`_.


Any feedback on this documentation is highly welcome, including bugs, typos
or things you think should be included but aren't. You can use `FIWARE SDC - GitHub issues`_
to provide feedback.

For documentation previous to release 4.4.2 please check the manuals at FIWARE
public wiki:

- `FIWARE SDC - Installation and Administration Guide`_
- `FIWARE SDC - User and Programmers Guide`_

https://github.com/telefonicaid/fiware-paas/pull/245


GEi overall description
=======================
The FIWARE Software Deployment and Configuration (SDC) GE is is the key enabler
used to support automated deployment (installation and configuration) of software
on running virtual machines. As part of the complete process of deployment of applications,
the aim of SDC GE is to deploy software product instances upon request of the
using the SDC API or through the Cloud Portal, which in turn uses the PaaS Manager GE (see `FIWARE PaaS Manager`_).

After that, users will be able to deploy artifacts, that are part of the application,
on top of the deployed product instances.

Build and Install
=================

The recommended procedure is to install using RPM packages in CentOS 6.x as it is explained in
the `following document <doc/installation-guide.rst#install-sdc-from-rpm#>`_
. If you are interested in building
from sources, check `this document <doc/installation-guide.rst#install-sdc-from-source#>`_.


Requirements
------------

- System resources: see `these recommendations
  <doc/installation-guide.rst#Resource availability>`_.
- Operating systems: CentOS (RedHat), being CentOS 6.5 the
  reference operating system.


Installation
------------

Using FIWARE package repository (recommended)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Refer to the documentation of your Linux distribution to set up the URL of the
repository where FIWARE packages are available (and update cache, if needed)::

    http://repositories.testbed.fiware.org/repo/rpm/x86_64

Then, use the proper tool to install the packages::

    $ sudo yum install fiware-sdc

and the latest version will be installed. In order to install a specific version::

    $ sudo yum install fiware-sdc-{version}-1.noarch

where {version} being the specific version to be installed


Upgrading from a previous version
---------------------------------

Unless explicitly stated, no migration steps are required to upgrade to a
newer version of the Software  Deployment and Configuration components:

- When using the package repositories, just follow the same directions
  described in the Installation_ section (the ``install`` subcommand also
  performs upgrades).
- When upgrading from downloaded package files, use ``rpm -U`` in CentOS

Upgrading database
~~~~~~~~~~~~~~~~~~
In case the database needs to be upgrade, the script db-changelog.sql should
be execute. To do that, it just needed to execute::

    psql -U postgres -d $db_name << EOF
    \i db-changelog.sql


Using installation script
~~~~~~~~~~~~~~~~~~~~~~~~~
The installation of fiware-sdc can be done in the easiest way by executing the script::

  scripts/bootstrap/centos.sh

The script will ask you the following data to configure the configuration properties:

- The database name for the fiware-sdc
- The postgres password of the database
- the keystone url to connect fiware-sdc for the authentication process
- the admin keystone user for the authentication process
- the admin password for the authentication process

Running
=======

As explained in the `GEi overall description`_ section, there are a variety of
elements involved in the Software Delivery and Configuration architecture, apart from those components
provided by this Software Delivery and Configuration GE (at least, an instance of configuration
engine like Chef server of Puppet master). Please
refer to their respective documentation for instructions to run them.


In order to start the software deployment and configuration service, as it is based on a
web applicatin on top of jetty, just you should run::

    $ service fiware-sdc start

Then, to stop the service, run::

    $ service fiware-sdc stop

We can also force a service restart::

    $ service fiware-sdc restart


Configuration file
------------------

The configuration of SDC is in configuration_properties table in the database.
There, it is required to configure::

    $ openstack-tcloud.keystone.url: This is the url where the keystone-proxy is deployed
    $ openstack-tcloud.keystone.user: the admin user
    $ openstack-tcloud.keystone.password: the admin password
    $ openstack-tcloud.keystone.tenant: the admin tenant
    $ sdc_manager_url: the final url, mainly https://sdc-ip:8443/sdc

In addition, to configue the SDC application inside the webserver, it is needed to change the context file.
To do that, change sdc.xml found in distribution file and store it in folder $SDC_HOME/webapps/::

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


Checking status
---------------

In order to check the status of the service, use the following command
(no special privileges required)::

    $ service fiware-sdc status


API Overview
============

The Software Deployment and Configuration offers a REST API, which it can be used for both
managing the software catalogue and the installation of software in virtual machines.

For instance, it is possible to obtain the software list in the catalogue with the
following curl::

  $ curl -v -H "Content-Type: application/json" -H "Accept: application/xml" -H "X-Auth-Token: your-token-id" -H "Tenant-Id: your-tenant-id"
    -X GET "https://saggita.lab.fi-ware.org:8443/sdc/rest/catalog/product"

Please have a look at the API Reference Documentation section bellow and at the programmer guide.

API Reference Documentation
---------------------------

- `FIWARE SDC v1 (Apiary) <https://jsapi.apiary.io/apis/fiwaresdc/reference.html>`_


Testing
=======

Unit tests
----------

The ``test`` target for each module in the SDC is used for running the unit tests in both components of
SDC GE. To execute the unit tests you just need to execute::

    mvn test

Please have a look at the section `building from source code
<doc/installation-guide.rst#install-sdc-from-source>`_ in order to get more
information about how to prepare the environment to run the
unit tests.


Acceptance tests
----------------

In the following path you will find a set of tests related to the
end-to-end funtionalities.

- `SDC Aceptance Tests <https://github.com/telefonicaid/fiware-sdc/tree/develop/test>`_

To execute the acceptance tests, go to the test/acceptance folder of the project and run::

  lettuce_tools --tags=-skip.

This command will execute all acceptance tests (see available params with the -h option)

End to End testing
------------------
Although one End to End testing must be associated to the Integration Test, we can show
here a quick testing to check that everything is up and running. It involves to obtain
the product information storaged in the catalogue. With it, we test that the service
is running and the database configure correctly::

   https://{SDC\_IP}:{port}/sdc/rest

The request to test it in the testbed should be::

  curl -v -k -H 'Access-Control-Request-Method: GET' -H 'Content-Type: application xml' -H 'Accept: application/xml'
  -H 'X-Auth-Token: 5d035c3a29be41e0b7007383bdbbec57' -H 'Tenant-Id: 60b4125450fc4a109f50357894ba2e28'
  -X GET 'https://localhost:8443/sdc/rest/catalog/product'

the option -k should be included in the case you have not changed the security configuration of SDC. The result should be the product catalog.

If you obtain a 401 as a response, please check the admin credentials and the connectivity from the sdc machine
to the keystone (openstack-tcloud.keystone.url in configuration_properties table)


Advanced topics
===============

- `Installation and administration <doc/installation-guide.rst>`_

  * `Software requirements <doc/installation-guide.rst#requirements>`_
  * `Building from sources <doc/installation-guide.rst/#install-sdc-from-source>`_
  * `Resources & I/O Flows <doc/installation-guide.rst#resource-availability>`_

- `User and programmers guide <doc/user_guide.rst>`_



License
=======

\(c) 2013-2015 Telefónica I+D, Apache License 2.0



.. REFERENCES

.. _FIWARE: http://www.fiware.org
.. _FIWARE Catalogue - Software Deployment and Configuration GE: http://catalogue.fiware.org/enablers/software-deployment-configuration-sagitta
.. _FIWARE SDC - GitHub issues: https://github.com/telefonicaid/fiware-sdc/issues/new
.. _FIWARE SDC - User and Programmers Guide: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Software_Deployment_%26_Configuration_-_User_and_Programmers_Guide
.. _FIWARE SDC - Installation and Administration Guide: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Software_Deployment_%26_Configuration_-_Installation_and_Administration_Guide
.. _FIWARE SDC - Apiary: https://jsapi.apiary.io/apis/fiwaresdc/reference.html
.. _FIWARE PaaS Manager: https://github.com/telefonicaid/fiware-paas







.. IMAGES

.. |Build Status| image:: https://travis-ci.org/telefonicaid/fiware-sdc.svg
   :target: https://travis-ci.org/telefonicaid/fiware-sdc
.. |Coverage Status| image:: https://coveralls.io/repos/telefonicaid/fiware-sdc/badge.png?branch=develop
   :target: https://coveralls.io/r/telefonicaid/fiware-sdc
.. |help stackoverflow| image:: http://b.repl.ca/v1/help-stackoverflow-orange.png
   :target: http://www.stackoverflow.com
