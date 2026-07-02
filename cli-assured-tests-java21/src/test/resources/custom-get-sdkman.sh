#!/usr/bin/env bash
#
# SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
# SPDX-License-Identifier: Apache-2.0
#

if [ -z "$SDKMAN_DIR" ]; then
    SDKMAN_DIR="$HOME/.sdkman"
    SDKMAN_DIR_RAW='$HOME/.sdkman'
else
    SDKMAN_DIR_RAW="$SDKMAN_DIR"
fi
export SDKMAN_DIR

echo "Installing SDKMAN! to $SDKMAN_DIR"

mkdir -p "$SDKMAN_DIR/bin"

cat << 'EOF' > "$SDKMAN_DIR/bin/sdkman-init.sh"
#!/usr/bin/env bash
#
# SPDX-FileCopyrightText: Copyright (c) 2025 CLI Assured contributors as indicated by the @author tags
# SPDX-License-Identifier: Apache-2.0
#


# A fake SDKMAN! stub for testing

function sdk() {

    COMMAND="$1"

    case "$COMMAND" in
    version)
        echo "Custom SDKMAN! version 1.2.3"
        ;;
    esac
}
EOF

echo "All done!"
