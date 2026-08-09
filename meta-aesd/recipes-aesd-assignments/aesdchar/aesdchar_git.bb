LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-mer0vech.git;protocol=ssh;branch=main \
           file://aesdchar-init.sh \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "14bee0c25e98464a9cdf81a977b8a9d1eb8615e4"

S = "${WORKDIR}/git/aesd-char-driver"
# MODULES_SUBDIR = "aesd-char-driver"

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit module
inherit update-rc.d

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "aesdchar-init"
INITSCRIPT_PARAMS:${PN} = "start 30 S . stop 10 0 1 6 ."

do_install () {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/aesdchar-init.sh ${D}${sysconfdir}/init.d/aesdchar-init

    install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra
    install -m 0644 ${S}/aesdchar.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/
}

FILES:kernel-module-aesdchar = "${base_libdir}/modules/${KERNEL_VERSION}/extra/aesdchar.ko"
FILES:${PN} = "${sysconfdir}/init.d/aesdchar-init"

INSANE_SKIP:${PN} = "installed-vs-shipped"
INSANE_SKIP:kernel-module-aesdchar = "installed-vs-shipped"

RDEPENDS:${PN} += "kernel-module-aesdchar"

