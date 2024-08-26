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
    QString response;

    if (reply->error() == QNetworkReply::NoError) {
        // Read all bytes
        QByteArray responseBytes = reply->readAll();
        if (responseBytes.contains('{') && responseBytes.contains('}'))
        {
            response = "Error on JSON";
        }else{
            response = QString::fromStdString(responseBytes.toStdString());
        }
        emit on_network_response(response);
    }else {
        if (reply->error() == QNetworkReply::UnknownNetworkError){
            response = "Unknown Error";
        }else{
            response = reply->errorString();
        }
        emit on_network_response(response);
    }
    reply->deleteLater();
}
