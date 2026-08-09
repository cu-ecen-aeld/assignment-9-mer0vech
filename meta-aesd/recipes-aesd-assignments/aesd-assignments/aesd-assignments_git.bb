# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit update-rc.d

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-mer0vech.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
SRCREV = "14bee0c25e98464a9cdf81a977b8a9d1eb8615e4"

S = "${WORKDIR}/git/server"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "ss-aesdsocket.sh"
INITSCRIPT_PARAMS:${PN} = "start 99 S . stop 20 0 1 6 ."
FILES:${PN} += "${bindir}/aesdsocket ${sysconfdir}/init.d/ss-aesdsocket.sh"

TARGET_CFLAGS += "-Wall -Wextra -pthread -DUSE_AESD_CHAR_DEVICE"
TARGET_LDFLAGS += "-pthread -lrt"
EXTRA_OEMAKE = "'CC=${CC}' 'CFLAGS=${TARGET_CFLAGS}' 'LDFLAGS=${TARGET_LDFLAGS}' 'TARGET=aesdsocket'"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
  install -d ${D}${bindir}
  install -d ${D}${sysconfdir}/init.d

  install -m 0755 ${S}/aesdsocket ${D}${bindir}/
  
  install -m 0755 ${S}/ss-aesdsocket.sh ${D}${sysconfdir}/init.d/
}
