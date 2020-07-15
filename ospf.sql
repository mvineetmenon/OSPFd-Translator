create table ospf_router (
	router_id inet,
	
	is_passive boolean,
	spf_delay_msec int,
	spf_init_holdtime_msec int,
	spf_max_holdtime_msec int
);

create table ospf_interfaces (		
	ospf_area_id int default null,
	ospf_authentication_id int default null,
	cost int,
	dead_interval int default 40,
	hello_interval int default 10,
	priority int default 1,	
	retransmit_interval int default 5,
	transit_delay int default 1
);

create table areas (
	id serial primary key,
	area_id inet,
	net_address inet,
	is_stub boolean,
	is_summary boolean,
	ospf_authentication_id int default null
);	

create type authentication_type as enum ('password', 'md5');

create table ospf_authentications (
	id serial primary key,	
	type authentication_type,
	value varchar default 'md5'
);

create type permitordeny as enum ('permit', 'deny');

create table route_maps (
	id serial primary key,
	route_map_name varchar not null,
	action permitordeny not null,
	order int not null,
	match_param varchar not null,
	match_value varchar not null,
	action_param varchar not null,
	action_value varchar not null,
	exit_action varchar
);

create type route_types as enum ('static', 'connected', 'bgp');

create table routes (
	id serial,
	is_enable boolean,
	redistribute_type route_types,
	metric int default null,
	route_map_id varchar references route_maps(id)
);



/* {
  "featureType" : "routing_4.0"
  "version" : 32,
  "enabled" : true,
  "routingGlobalConfig" : {
    "routerId" : 192.168.4.49,
    "ecmp" : false,
    "logging" : {
      "enable" : false,
      "logLevel" : "info"
    },
    "ipPrefixes" : [ {
      "name" : "a",
      "ipAddress" : "10.112.196.160/24",
  },
  "staticRouting" : {
    "defaultRoute" : {
      "vnic" : "0",
      "mtu" : 1500,
      "description" : "defaultRoute",
      "gatewayAddress" : "192.168.0.1"
    },
    "staticRoutes" : {
      "staticRoutes" : [ {
        "mtu" : 1500,
        "description" : "route1",
        "type" : "user",
        "vnic" : "0",
        "network" : "172.16.1.2/32",
        "nextHop" : "192.168.32.201"
        }
      } ]
  },
  "ospf" : {
    "enabled" : true,
    "protocolAddress" : null,
    "forwardingAddress" : null,
    "ospfAreas" : {
      "ospfAreas" : [ {
        "areaId" : 51,
        "type" : "nssa",
        "authentication" : {
          "type" : "none",
          "value" : null
        }
      }, 
    "ospfInterfaces" : {
      "ospfInterfaces" : [ 
          "vnic" : 0,
          "areaId" : 51,
          "helloInterval" : 10,
          "deadInterval" : 40,
          "priority" : 128,
          "cost" : 10,
          "mtuIgnore" : false,
        }
      } ]
    },
    "redistribution" : {
      "enabled" : true,
      "rules" : {
        "rules" : [ ]
      }
    },
    "gracefulRestart" : true,
    "defaultOriginate" : false
  },
  "bgp" : null
} */
