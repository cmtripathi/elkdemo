
The applications are having multiple systems with multiple logs. The logs don't provide only real-time analysis of the technical status of the software but could also provide some business information too.  So that to get the useful information form the logs we need to enters the ELK (ElasticSearch, LogStash, and Kibana) architecture:

Each tools provides different responsibilities:
*	**ElasticSearch**: This provides a clustered solution to make searches on a set of data.
*	**Logstash**: This is responsible for collecting the data, make transformations like parsing - using regular expressions - adding fields, formatting as structures like JSON, etc. and finally sending the data to various destinations, like an ElasticSearch cluster. 
*	**Kibana**: This is a web-based application to provide a light and easy-to-use dashboard tool. 

Lets start with **Logstash**:

LogStash installation
We have to download the LogStash, so open the terminal and enter:

curl https://download.elasticsearch.org/logstash/logstash/logstash-1.4.2.tar.gz | tar -xz

Now we have the logstash-1.4.2 folder in current directory.  We can make 4 types of configurations in LogStash:
- **Input**: On this configuration, we put the sources of our streams, that can range from polling files of a file system to more complex inputs such as a Amazon SQS queue and even Twitter.
- **Codec**: On this configuration we make transformations on the data, like turning into a JSON structure, or grouping together lines that are semantically related, like for example, a Java's stack trace.
- **Filter**: On this configuration we make operations such as parsing data from/to different formats, removal of special characters and checksums for DE duplication.
- **Output**: On this configuration we define the destinations for the processed data, such as an ElasticSearch cluster, AWS SQS, Nagios etc.

Please find the code to generate the logs from here. Now go to the logstash folder and create config.conf file with following entries:
```
input {
log4j {
port => 1500
type => "log4j"
tags => [ "technical", "log"]
}
}

output {
stdout { codec => rubydebug }
elasticsearch_http {
host => "localhost"
port => 9200
index => "log4jlogs"
}
}
```
ElasticSearch installation
We have to download the LogStash, so open the terminal and enter:
```
curl https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.4.4.tar.gz | tar -zx
```
After running the command, we will find a new folder called "elasticsearch-1.4.4" created on the same folder we run our command. To our example, we will create 2 copies of this folder on a folder we call "elasticsearchcluster", where each one will represent one node of the cluster. To do this, we run the following commands:
```
sudo cp -avr elasticsearch-1.4.4/ elasticsearchcluster/elasticsearch-1.4.4-node1/
sudo cp -avr elasticsearch-1.4.4/ elasticsearchcluster/elasticsearch-1.4.4-node2/
rm -R elasticsearch-1.4.4/
```
Now we have to start both nodes by calling 
```
./elasticsearch-1.4.4-node1/elasticsearch and ./elasticsearch-1.4.4-node1/elasticsearch & and ./elasticsearch-1.4.4-node1/elasticsearch and ./elasticsearch-1.4.4-node2/elasticsearch &
```
Now we have our cluster up and running, let's start to use it. Let's see the mappings of the index that ElasticSearch created for us, based on the configuration we made on LogStash. Let's open a terminal and run the following command:
```
curl -XGET 'localhost:9200/log4jlogs/_mapping?pretty'
```
The result would be:

 

Let's begin by searching all log messages, which the priority was "INFO". We make this searching by running:
```
curl -XGET 'localhost:9200/log4jlogs/log4j/_search?q=priority:info&pretty=true'
```
A fragment of the result would be something like the following:
 

Kibana installation
Let’s download Kibana from the site. To do this, let's open a terminal and type the following command:
```
curl https://download.elastic.co/kibana/kibana/kibana-4.0.0-darwin-x64.tar.gz
```
Please check the exact download link accordingly to your OS from the following site:
```
http://www.elasticsearch.org/overview/kibana/installation/
```
After running the command, we can see a folder called "kibana-4.0.0-darwin-x64". That's all we need to do to install Kibana on our lab. Before we start running, however, we need to start both Logstash and ElasticSearch, to make our whole stack go live.

One configuration needs to change that the elasticsearch_url property, inside the "/kibana-4.0.0-darwin-x64/config/kibana.yml" file. Since we didn't change the default port from our elasticsearch's cluster and we are running all the tools on the same machine, there is no need to change anything, but on a real world scenario, you may need to change this property to point to your elasticsearch's cluster. 

After we start the rest of the stack, it is time to run Kibana. To run it, we have to run following command from the terminal:
```
./kibana-4.0.0-darwin-x64/bin/kibana
```
Now, let's start using Kibana. Let's open a web browser and enter:
```
http://localhost:5601/
```
Now we can see the log under the discover tab:

 



References:
https://dzone.com/articles/elk-using-centralized-logging
https://dzone.com/articles/elk-using-centralized-logging-0
https://dzone.com/articles/elk-using-centralized-logging-1

