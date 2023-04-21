import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {
    ADOPTION_STATUS_OPTIONS,
    ADOPTION_TYPE_OPTIONS,
    AGE_OPTIONS,
    SEX_OPTIONS,
    SPECIES_OPTIONS,
    STATUS_OPTIONS
} from "../util/Enums";

export default function AdoptionDetails() {
    const {id} = useParams();
    const [adoption, setAdoption] = useState(null);
    const [isProperShelter, setIsProperShelter] = useState(false);

    function inviteUserToShelter(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/real/inviteRealAdoption/${adoptionId}`)
            .then((response) => {
                alert("Zostało wysłane zaproszenie do użytkownika z prośbą o kontakt/przybycie do schroniska");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił błąd z wysłaniem maila do użytkownika, potrzeba ręcznego kontaktu");
            });
    }

    function manualInviteUserToShelter(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/real/acceptManualInvited/${adoptionId}`)
            .then((response) => {
                alert("Użytkownik został zaproszony ręcznie, aktualizuje status adopcji");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }

    function userVisitedShelter(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/real/confirmVisit/${adoptionId}`)
            .then((response) => {
                alert("Użytkownik odwiedził schronisko, aktualizuje status adopcji");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }

    function acceptAdoption(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/real/complete/${adoptionId}`)
            .then((response) => {
                alert("Zwierzę zaadoptowane, aktualizuje status tej adopcji jak i pozostałcyh adopcji realnych zwierzęcia");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }

    function declineAdoption(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/real/decline/${adoptionId}`)
            .then((response) => {
                alert("Adopcja odrzucona, aktualizuje status tej adopcji jak i pozostałcyh adopcji realnych zwierzęcia");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }

    useEffect(() => {
        axios.defaults.withCredentials = true;
        axios
            .get(`http://localhost:8080/adoption/${id}`)
            .then((response) => {
                setAdoption(response.data);
            })
            .catch((error) => console.error("Error fetching animal data:", error));
    }, [id]);

    useEffect(() => {
        if (adoption) {
            setIsProperShelter(
                localStorage.getItem("userType") === "SHELTER" &&
                localStorage.getItem("userId") == adoption.animal.shelter.id
            );
        }
    }, [adoption]);

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10">
                    Szczegóły adopcji
                </h2>
                {adoption ? (


                    <>
                        <p>Zwierzę:</p>
                        <img src={adoption.animal.imagePath} alt="Obrazek"/>
                        <p>Imię: {adoption.animal.name}</p>

                        <p>Gatunek: {SPECIES_OPTIONS[adoption.animal.species]}</p>
                        <p>Płeć: {SEX_OPTIONS[adoption.animal.sex]}</p>
                        <p>Wiek: {AGE_OPTIONS[adoption.animal.age]}</p>
                        <p>Data urodzenia: {adoption.animal.birthDate}</p>
                        <p>Status: {STATUS_OPTIONS[adoption.animal.animalStatus]}</p>
                        <p>Dodatkowe informacje: {adoption.animal.information}</p>
                        <p>Adopcja:</p>
                        <p>Rodzaj adopcji: {ADOPTION_TYPE_OPTIONS[adoption.adoptionType]}</p>
                        <p>Status adopcji: {ADOPTION_STATUS_OPTIONS[adoption.adoptionStatus]}</p>
                        <p>Ważna do: {adoption.validUntil}</p>
                        <p>Użytkownik powiązany z adopcją</p>
                        <p>Imię: {adoption.user.firstName}</p>
                        <p>Nazwisko: {adoption.user.lastName}</p>
                        <p>Email: {adoption.user.email}</p>
                        <p>Nr telefonu: {adoption.user.address.phone}</p>

                        {isProperShelter && (adoption.adoptionStatus === 'REQUEST_REVIEW' || adoption.status === 'PENDING_SHELTER_INVITED' || adoption.status === 'PENDING') && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => inviteUserToShelter(adoption.id)}
                            >
                                Zaproś użytkownika do schroniska
                            </button>
                        )}
                        {isProperShelter && (adoption.adoptionStatus === 'REQUIRES_MANUAL_INVITATION') && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => manualInviteUserToShelter(adoption.id)}
                            >
                                Użytkownik zaproszony ręcznie/aktualizacja statusu
                            </button>
                        )}
                        {isProperShelter && (adoption.adoptionStatus === 'SHELTER_INVITED') && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => userVisitedShelter(adoption.id)}
                            >
                                Użytkownik odwiedził schronisko, aktualizuje status
                            </button>
                        )}
                        {isProperShelter && (adoption.adoptionStatus === 'VISITED') && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => acceptAdoption(adoption.id)}
                            >
                                Zwierzę zaadoptowane, aktualizuje status
                            </button>
                        )}
                        {isProperShelter && (adoption.adoptionStatus !== 'DECLINED') && (
                            <button
                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                onClick={() => declineAdoption(adoption.id)}
                            >
                                Adopcja odrzucona, aktualizuje status
                            </button>
                        )}
                    </>
                ) : (
                    <p>Ładowanie danych zwierzaka...</p>
                )}
            </div>
        </div>
    );
}
