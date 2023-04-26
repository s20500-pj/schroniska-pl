
## główne założenia

Projekt będzie posiadał dwa główne branche, develop i prod

- develop - rozwojowy(testowy)
- prod - produkcja(przetestowane, pewne zmiany)

W ten sposób w razie rozwalenia środowiska będziemy mogli cofnąć zmiany do poprzedniej wejsrji z działąjącego brancha.  
Nowe funkcjonalności będziemy pushować na brancha develop, tworzyć nowego merge requesta i po code review dopiero będzie on scalany z branchem develop. Dopiero po przetestowaniu wprowadzonych zmian będą scalane z branchem produkcyjnym.  
Najlepiej by było aby każda nowa funkcjonalność to był osobny branch, łatwiej będzie coś cofnąć w razie czego albo ocenić pracę kolegi/koleżanki.  

Frontend będzie się komunikował z backendem poprzez REST API wystawione przez backend. Jeśli front będzie czegoś potrzebował, np listy zwierzaków to będzie "strzelał" w odpowiedni endpoint.  

Najlepiej pracować w intelij, możemy odpalić sobie nasz backend i jednocześnie wejść w terminalu do folderu frontend i stamtąd odpalić npm start. W ten sposób mamy odpalone naraz dwa środowiska.  


## instrukcja github - tworzenie nowych branchy

git checkout develop - przechodzimy na brancha develop  
git pull develop - zaciągamy najnowsze zmiany z brancha develop, mamy aktualne środowisko  
git checkout -b feature/dodanie_endpointu_wyswietlajacego_liste_zwierzakow develop - tworzymy nowy branch z brancha develop(tworzymy jego kopie z inną nazwą)  
git add nazwa_pliku_do_zacommitowania - dodajemy plik jaki chcemy zacomitować  
git commit -m "dodanie nowego endpointu z listą zwierzaków, refractor" - commitujemy zmiany  
git push origin feature/dodanie_endpointu_wyswietlajacego_liste_zwierzakow - pushujemy nasze zmiany na githuba  

Wchodzimy na githbua, tworzymy nowego pull requesta, wybieramy nasz branch który chcemy scalić, a docelowy branch develop. Dajemy review na któregoś z kolegów i czekamy na akceptację merge requesta.  

W przypadku zaakceptowania jest on scalany z developem.  

Jeśli nasz merge request nie przejdzie code review to musimy się na niego przełączyć poleceniem git checkout, następnie wprowadzić poprawki, zacomitować i znowu spushować(nowe zmiany powinny wskoczyć na poprzedniego pull requesta). Dajemy znać oceniającemu o ponowne spojrzenie na kod.

## Odpalanie apki
1. Potrzebny jest Postgres z odpalonym serverem, jeśli odpalamy profil dev
2. Potrzebny jest Redis z odpalonym serwerem. By odpalić serwer command: redis-server (https://redis.io/docs/getting-started/installation/)
3. Zaimportować kolekcję i environment do postmana (backend/src/main/resources/postman)
4. Odpalić apke z profilu 'dev' (postgres z migracjami z załadowanymi danymi, potrzebne do admina) lub 'dev2' (h2 bez migracji = bez danych wstępnych)
5. endpointy wraz z wymaganymi requestami widoczne w postmanie: 
   * REJESTRACJA 
     * /registerUser, /registerShelter -> rejestracja usera, schroniska
     * przychodzi mail z linkiem potwierdzającym. Aktulanie serwis mailowy jest wyłączony. link dostępny w logach po zarejestrowaniu.
   * LOGOWANIE
     * /api/auth/authenticate -> logowanie. w odpowiedzi token zapisywany w postmanie -> token służy do autentykacji użytkownika. 
   * ZATWIERDZANIE SCHRONISKA
     * Trzeba zalogować się jako admin (a więc odpalić apkę z profilu 'dev' z postgresem)
     * /admin/getShelters -> zwraca listę wszystkich schronisk (te zaakceptowane i niezaakceptowane przez admina)
     * /admin/enableShelters -> akceptuje wybrane schroniska (w requeście lista z nazwa schroniska, email + adres). Response: zwraca listę niezaakceptowanych schronisk niezaakceptowanych przez admina.
   * DO UZUPEŁNIENIA 
6. Aplikacja posiada dokumentacje API stworzoną przy pomocy Swaggera. Aby się do niej dostać należy odpalić apkę i uderzeżyć w endpoint /shelter-doc (lub /swagger-ui)

7. karty płatnicze: https://developers.payu.com/pl/overview.html#:~:text=OAuth%20(client_secret)%3A%C2%A0%C2%A0%C2%A0%C2%A0%202ee86a66e5d97e3fadc400c9f19b065d-,3.2.2%20Testowe%20karty,-W%20celu%20przetestowania

8. ENCRYPTION_SECRET=v$c!y-ftDN$=X7fKY.uqgGna) - użyć jako zmiennej środowiskowej przy odpalaniu backendu



