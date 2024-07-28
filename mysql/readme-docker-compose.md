sudo service mysql stop #First stop the standalone guy to free 3306
docker-compose -f docker-compose-onlyMySQL.yml up
mysql –hlocalhost  -p3306 -uroot -pp@ssword
PostScript: the above command is causing problems, this one worked then:
mysql -h 127.0.0.1 -P 3306 -u root -p 
CREATE DATABASE corecruddb;
use corecruddb;
source createschema.sql;

The above is not successful because docker-componse takes care of that already.
For client call, fortunately this also worked, which I need to test K8s EGRESS;
mysql -h 192.168.13.128 -P 3306 -u root -p 
And also this worked:
mysql -h aimachine -P 3306 -u root -p
