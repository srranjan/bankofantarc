export INGRESS_HOST=$(kubectl get gtw $1 -n $2 -o jsonpath='{.status.addresses[0].value}')
export INGRESS_PORT=$(kubectl get gtw $1 -n $2 -o jsonpath='{.spec.listeners[?(@.name=="http")].port}')
#export SECURE_INGRESS_PORT=$(kubectl get gtw my-gateway -n $2 -o jsonpath='{.spec.listeners[?(@.name=="https")].port}')
echo INGRESS_HOST $INGRESS_HOST
echo INGRESS_PORT $INGRESS_PORT
#echo $SECURE_INGRESS_PORT
