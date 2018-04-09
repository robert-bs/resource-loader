# Aplikacja do pobierania zasobów internetowych

## Opis
Aplikacja udostępnia API w architekturze REST do pobierania przekazanych zasobów. Zasoby są przekazywany w formie URL. Przekazanie zasobu, po prawidłowej jego walidacji, wstawia zadanie pobrania zasobu do kolejki. Odbiorca kolejki pobiera zasoby asynchronicznie w kolejności w jakiej były przekazane do aplikacji, a następnie zapisuje je do bazy danych. Ponadto możliwe jest wylistowanie pobranych zasobów, pobranie wybranego zasobu z bazy i przeszukiwanie bazy zasobów pod kątem wybranego ciągu znaków.

## Serwer
Pliki konfiguracyjne i moduły zostały przygotowane dla serwera WildFly w wersji 10.0.0 z możliwością uruchomienia w wersji standalone. Przygotowana konfiguracja wymaga bazy danych Postgres z utworzoną bazą rrdb, użytkownikiem postgres i hasłem postgres. Konfiguracja kolejki JMS jest możliwa z poziomu ustawień serwera, jako system properties.<br>
<Br>
nazwa: <b>jms.queue</b><br>
wartość: nazwa przygotowanej kolejki w JNDI 

nazwa <b> jms.connectionFactory</b><br>
wartość: nazwa JMSConnectionFactory

## Wykorzystanie API

#### Pobieranie zasobów do aplikacji:<br>
metoda: POST<br>
url: <i>[adres_serwera]/resource-loader/rest/download</i><br>
typ: application/xml<br>
ciało: <pre><url\>[adres_zasobu]<url\></pre>

Odpowiedź:<br>
W przypadku prawidłowego zarejestrowania zadania do pobrania odesłana jest odpowiedź z kodem 200.
<br>
W przeciwnym razie odesłana jest odpowiedź z kodem 400:
<li>zbyt duży plik do pobrania</li>
<li>nie osiągalny adres zasobu</li>
<li>nieprawidłowo sformatowany URL</li>

#### Pobranie listy zasobów aplikacji:<br>
metoda: GET<br>
url: <i>[adres_serwera]/resource-loader/rest/resource/all</i><br>
<br>
Odpowiedź:<br>
Lista zasobów w formacie JSON:<br>
{"id":wartosc,"url":wartosc,"timestamp":data_utworzenia}<br>
W przypadku braku zasobów zostanie odesłana odpowiedź 400 z informacją o braku zasobów w systemie

#### Pobranie pojedyńczego zasobu z aplikacji<br>
metoda: <br>
url: <i>[adres_serwera]/resource-loader/rest/resource/one/{id}</i><br>

Odpowiedź:<br>
Wybrany plik zasobu lub kod 400 jeśli zasób nie został znaleziony<br>

#### Wyszukiwanie zasobów zawierających zadany ciąg znaków:

metoda: GET<br>
url: <i>[adres_serwera]/resource-loader/rest/resource/pattern/{pattern}</i><br>

Odpowiedź:<br>
Lista zasobów zawierających dany ciąg tekstu w formacie JSON lub kod 400 jeśli zasób nie został znaleziony

## Konfiguracja aplikacji
Aplikacja posiada kilka parametrów konfiguracyjnych zebranych w pliku application.properties

<b>downloadTimeoutSeconds</b> - liczba sekund, po których nastąpi timeout połączenia do zasobu

<b>downloadRetryMin</b> - czas po jakim ma nastąpić kolejna próba pobrania zasobu, jeśli był nieosiągalny

<b>downloadMaxRetry</b> - liczba prób ile maksymalnie aplikacja może próbować pobrać nieosiągalny zasób

<b>maxContentLength</b> - maksymalna długość pobieranego zasobu [bajty]

<b>searchForPatternWithStream</b> - przełącznik algorytmu wyszukiwania: false - korzysta z algorytmu Knutha-Morrisa-Pratta - konieczność ładowania całego zasobu, true - korzysta ze Scanner'a (działa słabo, lepsza wydajność na algorytmie KMP)

<b>downloadProxyUse</b> - true lub false czy korzystać z serwera proxy

<b>downloadProxyHost</b> - adres serwera proxy

<b>downloadProxyPort</b> - port serwera proxy

<b>downloadProxyUser</b> - nazwa użytkownika w przypadku konieczności autentykacji

<b>downloadProxyPassword</b> - hasło użytkownika jw.




