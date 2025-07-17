DESCRIPTION = "The backport version of the rtw88 drivers set for out-of-tree build"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

inherit module

SRC_URI += " \
    git://github.com/lwfinger/rtw88.git;protocol=https;branch=master \
    file://0001-Fix-the-makefile-so-Yocto-can-actually-DO-it-s-thing.patch \
    "
SRCREV = "fa96fd4c014fa528d1fa50318e97aa71bf4f473c"
S = "${WORKDIR}/git"

EXTRA_OEMAKE:append = " \
    KSRC=${STAGING_KERNEL_DIR} FWDIR=${D}${libdir}/firmware MODPROBEDIR=${D}/etc/modprobe.d \
    MODDESTDIR=${D}${libdir}/modules/${LINUX_VERSION}/kernel/drivers/net/wireless/rtw88 \
    "

# So...they don't handle the install in a standard manner for this tree, so we're going to make it work right
# for the way it's being built in the backport sources...
do_install() {
    install -d ${D}/etc/modprobe.d
    make install ${EXTRA_OEMAKE}
    make install_fw ${EXTRA_OEMAKE}
}

# Cheat- use the toplevels to tell Yocto to package it all up.
FILES:${PN} = "/etc /usr"