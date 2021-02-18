#
# This work is protected under copyrights held by the members of the
# TOOP Project Consortium as indicated at
# http://wiki.ds.unipi.gr/display/TOOP/Contributors
# (c) 2018-2021. All rights reserved.
#
# This work is dual licensed under Apache License, Version 2.0
# and the EUPL 1.2.
#
#  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
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
#  = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
#
# Licensed under the EUPL, Version 1.2 or – as soon they will be approved
# by the European Commission - subsequent versions of the EUPL
# (the "Licence");
# You may not use this work except in compliance with the Licence.
# You may obtain a copy of the Licence at:
#
#         https://joinup.ec.europa.eu/software/page/eupl
#

FROM tomcat:9-jdk11

ARG VERSION="2.1.2"
ARG WAR_NAME=tc-webapp-${VERSION}-SNAPSHOT.war

ARG CHASH=""
ENV COMMIT_HASH=${CHASH}

#create tc webapp folder
WORKDIR $CATALINA_HOME/webapps

ENV CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -Djava.security.egd=file:/dev/urandom"

COPY target/${WAR_NAME} ./

RUN rm -fr manager host-manager docs examples ROOT && \
    unzip $WAR_NAME -d ROOT  && \
    rm -fr $WAR_NAME
