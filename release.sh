#!/bin/bash
#
# SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
# SPDX-License-Identifier: Apache-2.0
#


# Release CLI Assured

set -e

if [ "$#" -eq  "2" ]; then
    releaseVersion="$1"
    array=($(echo "$releaseVersion" | tr . '\n'))
    releaseVersionMajorMinor="$((array[0])).$((array[1]))"
    nextVersion="$2"
elif [ "$#" -eq  "1" ]; then
    releaseVersion="$1"
    array=($(echo "$releaseVersion" | tr . '\n'))
    releaseVersionMajorMinor="$((array[0])).$((array[1]))"
    array[2]=$((array[2]+1))
    nextVersion="$(IFS=. ; echo "${array[*]}")-SNAPSHOT"
    echo "Setting default nextVersion ${nextVersion}"
else
    echo "One or two params expected: $0 <release-version>[ <next-development-version>]"
fi


: "${CENTRAL_USERNAME:?CENTRAL_USERNAME is not set}"
: "${CENTRAL_TOKEN:?CENTRAL_TOKEN is not set}"

# Latest java able to target bytecode level 8
sdk use java 11.0.25-tem

mvn release:clean release:prepare release:perform -B -DreleaseVersion=${releaseVersion} -DdevelopmentVersion=${nextVersion} -Prelease-prepare
