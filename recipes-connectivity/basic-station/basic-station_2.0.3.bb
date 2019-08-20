SUMMARY = "A LoRa packet forwarder developed and open sources by Semtech."
HOMEPAGE = "https://doc.sm.tc/station/"
BUGTRACKER = "https://github.com/lorabasics/basicstation/issues"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7706b51ea6d730e45568141c660072d5"

SECTION = "net"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PV = "2.0.3"
SRCREV = "c29b8502f8c715daecec6666835da6e981dc820a"
SRC_URI = "git://github.com/lorabasics/basicstation.git;branch=master;protocol=git \
           file://lorix-one.patch \
           file://init \
           file://station.conf \
           file://cups.uri \
          "

S = "${WORKDIR}/git"

DEPENDS = "lora-gateway mbedtls"

RUNDIR="/opt/lorix/clouds/basic-station"

inherit pkgconfig update-rc.d systemd

INITSCRIPT_NAME = "basic-station"
INITSCRIPT_PARAMS = "defaults 70"

SYSTEMD_SERVICE_${PN} = "basic-station.service"

#PARALLEL_MAKE = ""

#EXTRA_OEMAKE = " \
#	'CC=${CC}' 'CFLAGS=${CFLAGS} \
#	-I${S}/lora_pkt_fwd/inc \
#	-I${STAGING_DIR_TARGET}/${includedir}/lora-gateway/inc' \
#	'BUILDDIR=${S}' \
#	'LGW_PATH=${STAGING_DIR_TARGET}${libdir}/lora-gateway' \
#	"



do_compile () {
    oe_runmake platform=lorixone variant=std
}

do_install () {

    install -d ${D}${RUNDIR}

    install -m 0755 ${S}/build-lorixone-std/bin/station ${D}${RUNDIR}/station
    install -m 0444 ${S}/VERSION.txt ${D}${RUNDIR}/version.txt
    install -m 0444 ${FILE_DIRNAME}/files/station.conf ${D}${RUNDIR}/station.conf
    install -m 0444 ${FILE_DIRNAME}/files/cups.uri ${D}${RUNDIR}/cups.uri
    
    # init script
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}

FILES_${PN} = " \
    ${RUNDIR}/* \
    ${sysconfdir}/init.d/${INITSCRIPT_NAME} \
    "
