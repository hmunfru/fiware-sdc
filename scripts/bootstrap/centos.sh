#!/bin/sh


echo "Checking Linux Distribution before installing SDC"

yum install -y  redhat-lsb

linux_distro=` lsb_release -a | grep ID | awk -F " " '{print $3}'`

echo "$linux_distro"

if [ "$linux_distro" != "CentOS" ]; then
        echo "This script only works for CentOS linux distributions"
        exit
fi


echo "Installing Postgres"
yum install -y postgresql-server
chkconfig --add postgresql
chkconfig postgresql on
service postgresql initdb
service postgresql start

echo "Installing SDC"
FIWARE_REPOFILE="/etc/yum.repos.d/fiware.repo"

/bin/cat <<EOM >$FIWARE_REPOFILE
[Fiware]
name=FIWARE repository
baseurl=http://repositories.testbed.fi-ware.eu/repo/rpm/x86_64/
gpgcheck=0
enabled=1
EOM

yum install -y fiware-sdc

echo "Configuring Postgres"

db_name=
postgres_user_passwd=
echo -n "Enter SDC Database name [sdc] > "
read db_name
echo -n "Enter Postgres user password [postgres_passwd] > "
read postgres_user_passwd

echo "SDC Database name:$db_name"

sudo -u postgres sh << EOF1
username="postgres"
psql $dbname $username << EOF
alter user postgres with password '$postgres_user_passwd';
create database $db_name;
grant all privileges on database $db_name to postgres;
\q
EOF
EOF1

echo "Modifying pg_hba.conf"
pg_hba_file=`find / -name pg_hba.conf`
sed -i 's/ident/md5/g' $pg_hba_file

echo "Modifying postgresql.conf"
postgresql_file=`find / -name postgresql.conf`
sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '0.0.0.0'/g" $postgresql_file

echo "Reloading SDCr Database"
service postgresql reload

sudo -u postgres sh << EOF1
cd /opt/fiware-sdc/resources
psql -U postgres -d $db_name << EOF
\i db-initial.sql
\i db-changelog.sql
\q
EOF
EOF1


echo "Configuring SDC"
echo "Modifying sdc.xml"
sdc_xml_file="/opt/fiware-sdc/webapps/sdc.xml"
sed -i "s/User\">postgres</User\">postgres</g" $sdc_xml_file
sed -i "s/Password\">postgres</Password\">$postgres_user_passwd</g" $sdc_xml_file
sed -i "s/DatabaseName\">sdc_int_cookbooks/DatabaseName\">$db_name/g" $sdc_xml_file
sed -i "s/ServerName\">130.206.80.119</ServerName\">localhost</g" $sdc_xml_file


echo "SDC being a service"
SDC_SERVICEFILE="/etc/init.d/fiware-sdc"

/bin/cat <<EOM >$SDC_SERVICEFILE
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
    case "\$1" in 
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
EOM

chmod 777 $SDC_SERVICEFILE

echo "Starting SDC as a Service"

chkconfig --add fiware-sdc
chkconfig fiware-sdc on
service fiware-sdc start

echo "Configuring SDC"
keystone_url=
keystone_user=
keystone_passwd=
tenant_id=
public_ip=`curl -s checkip.dyndns.org | sed -e 's/.*Current IP Address: //' -e 's/<.*$//'`
sdc_manager_url="https://$public_ip:8443/sdc/rest"
user_data_path="/opt/fiware-sdc/resources/userdata"

echo -n "Enter Keystone url > "
read keystone_url
echo -n "Enter Keystone user > "
read keystone_user
echo -n "Enter Keystone passwd > "
read keystone_passwd
echo -n "Enter TenantID > "
read tenant_id

echo "keystone_url:$keystone_url"
echo "keystone_user:$keystone_user"
echo "keystone_passwd:$keystone_passwd"
echo "tenant_id:$tenant_id"
echo "user_data_path:$user_data_path"
echo "sdc_manager_url:$sdc_manager_url"


sudo -u postgres sh << EOF1
psql -U postgres -d $db_name << EOF
UPDATE configuration_properties SET value='$user_data_path' where key='user_data_path';
UPDATE configuration_properties SET value='$sdc_manager_url' where key='sdc_manager_url';
UPDATE configuration_properties SET value='$keystone_url' where key='openstack-tcloud.keystone.url';
UPDATE configuration_properties SET value='$keystone_user' where key='openstack-tcloud.keystone.user';
UPDATE configuration_properties SET value='$keystone_passwd' where key='openstack-tcloud.keystone.pass';
UPDATE configuration_properties SET value='$tenant_id' where key='openstack-tcloud.keystone.tenant';
\q
EOF
EOF1

echo "Restarting PostGres"
service postgresql restart

echo "End of installation"




