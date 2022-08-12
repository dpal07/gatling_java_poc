#!/bin/bash

LIST_OUTPUT=$(aws configure list)
AWS_PROFILE=${AWS_PROFILE:Developers_tf-569806434804}

if (( $? == 0 )); then
    ACCESS_KEY=$(echo "$LIST_OUTPUT" | awk '/access_key/ { print $2 }' | tr -d '*')
    if [ "$ACCESS_KEY"  = "<not" ]; then
        echo "ACCESS_KEY not set, run 'aws configure list' to see values not set."
        echo "Without ACCESS_KEY values there, this script will not work."
        echo ""
        echo "Please login with 'aws sso login --profile <profile>' OR"
        echo "if direnv setup, 'aws sso login' will suffice."

        exit
    fi
    CREDENTIAL_FILES=( $(grep -l "$ACCESS_KEY" ~/.aws/cli/cache/*json) )
    SECRET_KEY=$(echo "$LIST_OUTPUT" | awk '/secret_key/ { print $2 }' | tr -d '*')
    CREDENTIAL_FILES=( $(grep -l "$SECRET_KEY" "${CREDENTIAL_FILES[@]}") )
    case "${#CREDENTIAL_FILES[@]}" in
        0)
            echo "Could not find file containing the access key $ACCESS_KEY. This is most likely"
            echo "a bug in the script."
            ;;
        1)
            echo "Found credentials file at ${CREDENTIAL_FILES[0]}"
            ACCESS_KEY="$(grep -o 'AccessKeyId": "[^"]*"' "${CREDENTIAL_FILES[0]}" | cut -d'"' -f 3)"
            SECRET_KEY="$(grep -o 'SecretAccessKey": "[^"]*"' "${CREDENTIAL_FILES[0]}" | cut -d'"' -f 3)"
            SESSION_TOKEN="$(grep -o 'SessionToken": "[^"]*"' "${CREDENTIAL_FILES[0]}" | cut -d'"' -f 3)"
            cat > ~/.tmp-creds.$AWS_PROFILE <<EOF
[${AWS_PROFILE}]
aws_access_key_id = $ACCESS_KEY
aws_secret_access_key = $SECRET_KEY
aws_session_token = $SESSION_TOKEN
EOF
            echo "Credentials saved to tmp-creds. Now run:"
            if [[ $AWS_PROFILE == tmpprofile ]]; then
                echo 'export AWS_PROFILE=tmpprofile'
            fi
            echo "export AWS_SHARED_CREDENTIALS_FILE=$HOME/.tmp-creds.$AWS_PROFILE"
            echo "so the AWS CLI will use this file."
            ;;
        *)
            echo "Multiple files found for access key $ACCESS_KEY and secret key $SECRET_KEY. This"
            echo "really should happen once every A HUGE NUMBER of times. I can't really offer any"
            echo "solution, other than running 'aws sso login' again to request a new key"
            ;;
    esac
else
    echo 'An error occurred (see error above). Environment variables not changed'
fi