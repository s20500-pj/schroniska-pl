
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





