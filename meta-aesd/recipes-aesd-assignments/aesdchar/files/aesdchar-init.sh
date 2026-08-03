#!/bin/sh

MODULE="aesdchar"
DEVICE="aesdchar"
MODE="664"

KERNEL_VERSION=$(uname -r)
MODULE_PATH="/lib/modules/$KERNEL_VERSION/extra/${MODULE}.ko"


case "$1" in
  start)
    if grep -q '^staff:' /etc/group; then
      GROUP="staff"
    else
      GROUP="wheel"
    fi

    if [ -e ${MODULE}.ko ]; then
      printf "Loading %s.ko module...\n" "$MODULE"
      insmod ./$MODULE.ko $* || exit 1
    else
      printf "%s.ko not found, attempting to modprobe" "$MODULE"
      modprobe ${MODULE} || exit 1
    fi

    MAJOR=$(awk "\$2==\"$MODULE\" {print \$1}" /proc/devices)
    if [ -n "$MAJOR" ]; then
      rm -f /dev/${DEVICE}
      mknod /dev/${DEVICE} c $MAJOR 0
      chgrp $GROUP /dev/${DEVICE}
      chmod $MODE  /dev/${DEVICE}
    else
      printf "Error: Module %s not found in /proc/devices.\n" "$MODULE"
      exit 1
    fi
    ;;
    
  stop)
    printf "Unloading %s.ko module...\n" "$MODULE"
    rmmod $MODULE || exit 1
    rm -f /dev/${DEVICE}
    ;;
    
  *)
    printf "Usage: $0 {start|stop}\n"
    exit 1
esac

exit 0

