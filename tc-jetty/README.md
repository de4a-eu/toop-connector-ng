# tc-jetty

Standalone version of TOOP Connector NG

Usage:

```
Usage: tc-jetty [-hV] [-p Port] [-s Stop Port] command
Standalone TOOP Connector NG
      command                What to do. Options are start, stop
  -h, --help                 Show this help message and exit.
  -p, --port Port            Port to run the TOOP Connector on (default: 8080)
  -s, --stopPort Stop Port   Internal port to watch for the shutdown command
                               (default: 8079)
  -V, --version              Print version information and exit.
```

Start via `start` and shut it down with `stop`.

How to run it:

```
java -jar tc-jetty-2.0.0-beta5-full.jar start
```

to add a custom configuration file add the propert "config.file" like this:

```
java -Dconfig.file=/path/to/your/file.properties -jar tc-jetty-2.0.0-beta5-full.jar start
```
