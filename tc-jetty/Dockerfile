#
# Copyright (C) 2018-2020 toop.eu
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM tomcat:8-jre8

ARG VERSION="2.0.0-beta5"
ARG JAR_NAME=tc-jetty-${VERSION}-full.jar

#create tc folder
WORKDIR /tc-jetty

ENV JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/urandom" \
    JAR_NAME="${JAR_NAME}"

RUN wget https://oss.sonatype.org/service/local/repositories/releases/content/eu/toop/tc-jetty/${VERSION}/${JAR_NAME}
#ADD ./target/${JAR_NAME} ./

ENTRYPOINT ["java", "-jar", "tc-jetty-2.0.0-beta5-full.jar"]
CMD ["start"]
