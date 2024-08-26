QT += core gui
QT += serialport
QT += charts
QT += network
QT += sql

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    apifacade.cpp \
    dbcontroller.cpp \
    dbfacade.cpp \
    jsontocsvconverter.cpp \
    main.cpp \
    mainwindow.cpp \
    protocol.cpp \
    serialconnection.cpp \
    serialfacade.cpp \
    serialmanager.cpp \
    serialportreader.cpp \
    serialportwriter.cpp

HEADERS += \
    apifacade.h \
    dbcontroller.h \
    dbfacade.h \
    jsontocsvconverter.h \
    mainwindow.h \
    protocol.h \
    serialconnection.h \
    serialfacade.h \
    serialmanager.h \
    serialportreader.h \
    serialportwriter.h

FORMS += \
    mainwindow.ui

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target
