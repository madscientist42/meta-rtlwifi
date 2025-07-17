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
    KSRC=${STAGING_KERNEL_DIR} FWDIR=${D}${libdir}/firmware/rtw88 MODPROBEDIR=${D}/etc/modprobe.d \
    MODDESTDIR=${D}${libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless/rtw88 \
    "

# So...they don't handle the install in a standard manner for this tree, so we're going to make it work right
# for the way it's being built in the backport sources...and therefore package up right.
do_install() {
    install -d ${D}/etc/modprobe.d
    make install ${EXTRA_OEMAKE}
    make install_fw ${EXTRA_OEMAKE}
}

# Specify out the additional packages we provide- since the FW is supposed
# to be in lock-step, we provide them in a different packaging and RDEPEND
# them in accordingly.  One-stop-shop.
PACKAGES += " \
    ${PN}-config \
    ${PN}-rtw8703b-fw \
    ${PN}-rtw8723d-fw \
    ${PN}-rtw8812a-fw \
    ${PN}-rtw8814a-fw \
    ${PN}-rtw8821a-fw \
    ${PN}-rtw8821c-fw \
    ${PN}-rtw8822b-fw \
    ${PN}-rtw8822c-fw \
    "

FILES:${PN}-config = "/etc"
FILES:${PN}-rtw8703b-fw = "${libdir}/firmware/rtw88/rtw8703b*"
FILES:${PN}-rtw8723d-fw = "${libdir}/firmware/rtw88/rtw8723d*"
FILES:${PN}-rtw8812a-fw = "${libdir}/firmware/rtw88/rtw8812a*"
FILES:${PN}-rtw8814a-fw = "${libdir}/firmware/rtw88/rtw8814a*"
FILES:${PN}-rtw8821a-fw = "${libdir}/firmware/rtw88/rtw8821a*"
FILES:${PN}-rtw8821c-fw = "${libdir}/firmware/rtw88/rtw8821c*"
FILES:${PN}-rtw8822b-fw = "${libdir}/firmware/rtw88/rtw8822b*"
FILES:${PN}-rtw8822c-fw = "${libdir}/firmware/rtw88/rtw8822c*"

# Declare out common interface core modules for easier RDEPENDing.
CORE = "kernel-module-rtw-core-${KERNEL_VERSION}"
PCIE = "kernel-module-rtw-pci-${KERNEL_VERSION}"
SDIO = "kernel-module-rtw-sdio-${KERNEL_VERSION}"
USB = "kernel-module-rtw-usb-${KERNEL_VERSION}"

# Declare out our chipset common core modules for the same reasons.
RTW_8723D = "kernel-module-rtw-8723d-${KERNEL_VERSION}"
RTW_8723CS = "kernel-module-rtw-8723cs-${KERNEL_VERSION}"
RTW_8723X = "kernel-module-rtw-8723x-${KERNEL_VERSION}"
RTW_8812A = "kernel-module-rtw-8812a-${KERNEL_VERSION}"
RTW_8814A = "kernel-module-rtw-8814a-${KERNEL_VERSION}"
RTW_8821A = "kernel-module-rtw-8821a-${KERNEL_VERSION}"
RTW_8821C = "kernel-module-rtw-8821c-${KERNEL_VERSION}"
RTW_8822B = "kernel-module-rtw-8822b-${KERNEL_VERSION}"
RTW_8822C = "kernel-module-rtw-8822c-${KERNEL_VERSION}"

# Each one of the kernel modules that follows potentially implicates one
# or more of the above RDEPENDs macros accordingly
RDEPENDS:kernel-module-rtw-8703b-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8703b-fw"
RDEPENDS:kernel-module-rtw-8723d-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8723d-fw ${RTW_8723CS} ${RTW_8723X}"
RDEPENDS:kernel-module-rtw-8723de-${KERNEL_VERSION} += "${CORE} ${RTW_8723D} ${PCIE}"
RDEPENDS:kernel-module-rtw-8723du-${KERNEL_VERSION} += "${CORE} ${RTW_8723D} ${USB}"
RDEPENDS:kernel-module-rtw-8723ds-${KERNEL_VERSION} += "${CORE} ${RTW_8723D} ${SDIO}"
RDEPENDS:kernel-module-rtw-8812a-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8812a-fw"
RDEPENDS:kernel-module-rtw-8812au-${KERNEL_VERSION} += "${CORE} ${RTW_8812A} ${USB}"
RDEPENDS:kernel-module-rtw-8814a-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8814a-fw"
RDEPENDS:kernel-module-rtw-8814ae-${KERNEL_VERSION} += "${CORE} ${RTW_8814A} ${PCIE}"
RDEPENDS:kernel-module-rtw-8814au-${KERNEL_VERSION} += "${CORE} ${RTW_8814A} ${USB}"
RDEPENDS:kernel-module-rtw-8821a-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8821a-fw"
RDEPENDS:kernel-module-rtw-8821au-${KERNEL_VERSION} += "${CORE} ${RTW_8821A} ${USB}"
RDEPENDS:kernel-module-rtw-8821c-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8821c-fw"
RDEPENDS:kernel-module-rtw-8821ce-${KERNEL_VERSION} += "${CORE} ${RTW_8821C} ${PCIE}"
RDEPENDS:kernel-module-rtw-8821cs-${KERNEL_VERSION} += "${CORE} ${RTW_8821C} ${SDIO}"
RDEPENDS:kernel-module-rtw-8821cu-${KERNEL_VERSION} += "${CORE} ${RTW_8821C} ${USB}"
RDEPENDS:kernel-module-rtw-8822b-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8822b-fw"
RDEPENDS:kernel-module-rtw-8822be-${KERNEL_VERSION} += "${CORE} ${RTW_8822B} ${PCIE}"
RDEPENDS:kernel-module-rtw-8822bs-${KERNEL_VERSION} += "${CORE} ${RTW_8822B} ${SDIO}"
RDEPENDS:kernel-module-rtw-8822bu-${KERNEL_VERSION} += "${CORE} ${RTW_8822B} ${USB}"
RDEPENDS:kernel-module-rtw-8822c-${KERNEL_VERSION} += "${PN}-config ${PN}-rtw8822c-fw"
RDEPENDS:kernel-module-rtw-8822ce-${KERNEL_VERSION} += "${CORE} ${RTW_8822C} ${PCIE}"
RDEPENDS:kernel-module-rtw-8822cs-${KERNEL_VERSION} += "${CORE} ${RTW_8822C} ${SDIO}"
RDEPENDS:kernel-module-rtw-8822cu-${KERNEL_VERSION} += "${CORE} ${RTW_8822C} ${USB}"

# IF someone includes our recipe as a packaging, force the install of all of it.
RDEPENDS:${PN} = " \
    kernel-module-rtw-8703b-${KERNEL_VERSION} \
    kernel-module-rtw-8723de-${KERNEL_VERSION} \
    kernel-module-rtw-8723du-${KERNEL_VERSION} \
    kernel-module-rtw-8723ds-${KERNEL_VERSION} \
    kernel-module-rtw-8812au-${KERNEL_VERSION} \
    kernel-module-rtw-8814ae-${KERNEL_VERSION} \
    kernel-module-rtw-8814au-${KERNEL_VERSION} \
    kernel-module-rtw-8821au-${KERNEL_VERSION} \
    kernel-module-rtw-8821ce-${KERNEL_VERSION} \
    kernel-module-rtw-8821cs-${KERNEL_VERSION} \
    kernel-module-rtw-8821cu-${KERNEL_VERSION} \
    kernel-module-rtw-8822be-${KERNEL_VERSION} \
    kernel-module-rtw-8822bs-${KERNEL_VERSION} \
    kernel-module-rtw-8822bu-${KERNEL_VERSION} \
    kernel-module-rtw-8822ce-${KERNEL_VERSION} \
    kernel-module-rtw-8822cs-${KERNEL_VERSION} \
    kernel-module-rtw-8822cu-${KERNEL_VERSION} \
    "