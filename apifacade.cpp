#include "apifacade.h"

ApiFacade::ApiFacade(QObject *parent) : QObject{parent}
{

}

void ApiFacade::sendMeasurements(const QByteArray &measurements)
{
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    // Build the request network
    QNetworkRequest request;
    QString url = QString::fromLatin1(ENDPOINT_ROOT);
    url = url + PREDICT;
    QUrl my_url = QUrl(url);
    request.setUrl(my_url);

    // Send POST request
    reply = manager->post(request, measurements);
    connect(reply, &QNetworkReply::finished, this, &ApiFacade::handle_network_reply);
}

void ApiFacade::handle_network_reply()
{
    qDebug() << reply->error();
    if (reply->error() == QNetworkReply::NoError) {
        // Read all bytes
        QByteArray responseBytes = reply->readAll();
        // TODO - convert QByteArray to String Json
        QString response = QString::fromStdString(responseBytes.toStdString());
        emit on_network_response(response);
    }else {
        emit on_network_response(reply->errorString());
    }
    reply->deleteLater();
}
