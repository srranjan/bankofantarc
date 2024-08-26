export INGRESS_HOST=$(kubectl get gtw $1 -n $2 -o jsonpath='{.status.addresses[0].value}')
export INGRESS_PORT=$(kubectl get gtw $1 -n $2 -o jsonpath='{.spec.listeners[?(@.name=="http")].port}')
echo INGRESS_HOST $INGRESS_HOST
echo INGRESS_PORT $INGRESS_PORT
#echo $SECURE_INGRESS_PORT
export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
echo GATEWAY_URL $GATEWAY_URL
