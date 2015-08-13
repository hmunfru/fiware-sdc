==================
FI-WARE SDC
============
| |Build Status| |Coverage Status| |help stackoverflow|

Introduction
=======================
This is the code repository for FIWARE Saggita, the reference implementation
of the Software Deployment and Configuration GE.

This project is part of FIWARE_. Check also the
`_FIWARE Catalogue - Software Deployment and Configuration GE`__.

Any feedback on this documentation is highly welcome, including bugs, typos
or things you think should be included but aren't. You can use `github issues`__
to provide feedback.

__ `FIWARE SDC - GitHub issues`_

For documentation previous to release 4.4.2 please check the manuals at FIWARE
public wiki:

- `FIWARE SDC - Installation and Administration Guide`_
- `FIWARE SDC - User and Programmers Guide`_


GEi overall description
=======================
The FIWARE Software Deployment and Configuration (SDC) GE is is the key enabler
used to support automated deployment (installation and configuration) of software
on running virtual machines. As part of the complete process of deployment of applications,
the aim of SDC GE is to deploy software product instances upon request of the
using the SDC API or through the Cloud Portal, which in turn uses the PaaS Manager GE (see PaaS Manager__).
__ `FIWARE PaaS Manager`_
After that, users will be able to deploy artifacts, that are part of the application,
on top of the deployed product instances

Components
----------
Chef server

Puppet master

Puppet wrapper

Build and Install
=================

The recommended procedure is to install using RPM packages in CentOS 6.x
. If you are interested in building
from sources, check `this document <doc/installation-guide.rst#install-sdc-from-rpm#>`_.


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
repository where FIWARE packages are available (and update cache, if needed):

    http://repositories.testbed.fiware.org/repo/rpm/x86_64


Then, use the proper tool to install the packages

    $ sudo yum install fiware-sdc

and the latest version will be installed. In order to install a specific version

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
be execute. To do that, it just needed to execute

    psql -U postgres -d $db_name << EOF
    \i db-changelog.sql


Running
=======

As explained in the `overall description`__ section, there are a variety of
elements involved in the Software Delivery and Configuration architecture, apart from those components
provided by this Software Delivery and Configuration GE (at least, an instance of configuration
engine like Chef server of Puppet master). Please
refer to their respective documentation for instructions to run them.

__ `GEi overall description`_


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
There, it is required to configure:

openstack-tcloud.keystone.url: This is the url where the keystone-proxy is deployed
openstack-tcloud.keystone.user: the admin user
openstack-tcloud.keystone.password: the admin password
openstack-tcloud.keystone.tenant: the admin tenant
sdc_manager_url: the final url, mainly http://sdc-ip:8080/sdc



Checking status
---------------

In order to check the status of the adapter service, use the following command
(no special privileges required):

::

    $ service fiware-sdc status


API Overview
============

- `FIWARE SDC v1 (Apiary)`__

__ `_FIWARE SDC - Apiary`_


Testing
=======

Unit tests
----------

The ``test`` target for each module in the SDC is used for running the unit tests in both components of
SDC GE:

Please have a look at the section `building from source code
<doc/installation-guide.rst#install-sdc-from-source>`_ in order to get more
information about how to prepare the environment to run the
unit tests.


Acceptance tests
----------------

In the following path you will find a set of tests related to the
end-to-end funtionalities.

- `SDC Aceptance Tests <https://github.com/telefonicaid/fiware-sdc/tree/develop/test>`_


Advanced topics
===============

- `Installation and administration <doc/manuals/admin/README.rst>`_

  * `Building from sources <doc/manuals/admin/build_source.rst>`_
  * `Running Adapter from command line <doc/manuals/admin/README.rst#from-the-command-line>`_
  * `Logs <doc/manuals/admin/logs.rst>`_
  * `Resources & I/O Flows <doc/manuals/admin/README.rst#resource-availability>`_

- `User and programmers guide <doc/manuals/user/README.rst>`_

  * `NGSI Adapter custom probe parsers <doc/manuals/user/README.rst#ngsi-adapter-parsers>`_
  * `Retrieval of historical data <doc/manuals/user/README.rst#monitoring-api>`_


License
=======

\(c) 2013-2015 Telefónica I+D, Apache License 2.0



.. REFERENCES

.. _FIWARE: http://www.fiware.org
.. _FIWARE Catalogue - Software Deployment and Configuration GE: http://catalogue.fiware.org/enablers/software-deployment-configuration-sagitta
.. _FIWARE SDC - GitHub issues: https://github.com/telefonicaid/fiware-sdc/issues/new
.. _FIWARE SDC - User and Programmers Guide: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Monitoring_-_User_and_Programmers_Guide
.. _FIWARE SDC - Installation and Administration Guide: https://forge.fiware.org/plugins/mediawiki/wiki/fiware/index.php/Monitoring_-_Installation_and_Administration_Guide
.. _FIWARE SDC - Apiary: https://jsapi.apiary.io/apis/fiwaresdc/reference.html
.. _FIWARE PaaS Manager: https://github.com/telefonicaid/fiware-paas
.. _Nagios: http://www.nagios.org/
.. _Zabbix: http://www.zabbix.com/
.. _openNMS: http://www.opennms.org/
.. _perfSONAR: http://www.perfsonar.net/






.. IMAGES

.. |Build Status| image:: https://travis-ci.org/telefonicaid/fiware-sdc.svg
   :target: https://travis-ci.org/telefonicaid/fiware-sdc
.. |Coverage Status| image:: https://coveralls.io/repos/telefonicaid/fiware-sdc/badge.png?branch=develop
   :target: https://coveralls.io/r/telefonicaid/fiware-sdc
.. |help stackoverflow| image:: http://b.repl.ca/v1/help-stackoverflow-orange.png
   :target: http://www.stackoverflow.com
