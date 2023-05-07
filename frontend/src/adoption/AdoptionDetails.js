import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {
    ADOPTION_STATUS_OPTIONS,
    ADOPTION_TYPE_OPTIONS,
    AGE_OPTIONS,
    SEX_OPTIONS,
    SPECIES_OPTIONS,
    ANIMAL_STATUS_OPTIONS
} from "../util/Enums";
import icon from "../dog-cat-icon.jpeg";

export default function AdoptionDetails() {
    axios.defaults.withCredentials = true;
    const {id} = useParams();
    const [adoption, setAdoption] = useState(null);
    const [isProperShelter, setIsProperShelter] = useState(false);
    const [isProperUser, setIsProperUser] = useState(false);
    let navigate = useNavigate();
    const placeholderImage = icon;
    const onImageError = (e) => {
        e.target.src = placeholderImage
    }
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
                alert("Adopcja odrzucona, aktualizuje status tej adopcji jak i pozostałych adopcji realnych zwierzęcia");
                setAdoption(response.data);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }
    function deleteAdoption(adoptionId) {
        axios
            .get(`http://localhost:8080/adoption/delete/${adoptionId}`)
            .then(() => {
                alert("Adopcja usunięta z systemu");
                navigate("/shelterRealAdoptionList");
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Wystąpił nieznany błąd");
            });
    }

    useEffect(() => {
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
            setIsProperUser(
                localStorage.getItem("userType") === "PERSON" &&
                localStorage.getItem("userId") == adoption.user.id
            );
        }
    }, [adoption]);

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-5 h-fit">
                    Szczegóły adopcji
                </h2>
                <div className='lg:flex justify-evenly md:block'>
                {adoption ? (
                    <>
                        <div className='px-10 py-5'>
                            <img src={'/' + adoption.animal.imagePath ? '/' + adoption.imagePath : placeholderImage}
                                 onError={onImageError}
                                 alt="Zdjęcie zwierzaka"
                                 className="shadow-xl border-2 border-orange rounded-xl object-cover h-[350px] w-[500px]"/>
                        </div>
                        <div className="flex justify-center py-5">
                        <div>
                            <p className=' text-xl font-bold text-brown'>Imię: </p>
                            <p className=' text-xl font-bold text-orange pb-5'>{adoption.animal.name}</p>
                            <p className='font-bold pt-2'>Gatunek:</p><p> {SPECIES_OPTIONS[adoption.animal.species]}</p>
                            <p className='font-bold pt-2'>Płeć: </p><p>{SEX_OPTIONS[adoption.animal.sex]}</p>
                            <p className='font-bold pt-2'>Wiek:</p><p> {AGE_OPTIONS[adoption.animal.age]}</p>
                            <p className='font-bold pt-2'>Data urodzenia: </p><p>{adoption.animal.birthDate}</p>
                            <p className='font-bold pt-2'>Status:</p><p> {ANIMAL_STATUS_OPTIONS[adoption.animal.animalStatus]}</p>
                            <p className='font-bold pt-2'>Dodatkowe informacje:</p><p> {adoption.animal.information}</p>
                        </div>
                        <div className='ml-10'>
                            <p className='text-xl font-bold text-brown'>Adopcja</p>
                            <p className='font-bold pt-2'>Rodzaj adopcji: </p><p> {ADOPTION_TYPE_OPTIONS[adoption.adoptionType]}</p>
                            <p className='font-bold pt-2'>Status adopcji: </p><p> {ADOPTION_STATUS_OPTIONS[adoption.adoptionStatus]}</p>
                            <p className='font-bold pt-2'>Ważna do: </p><p> {adoption.validUntil}</p>
                            <p className='text-xl font-bold text-brown pt-3'>Użytkownik powiązany z adopcją </p>
                            <p className='font-bold pt-2'>Imię: </p><p> {adoption.user.firstName}</p>
                            <p className='font-bold pt-2'>Nazwisko: </p><p> {adoption.user.lastName}</p>
                            <p className='font-bold pt-2'>Email: </p><p> {adoption.user.email}</p>
                            <p className='font-bold pt-2'>Nr telefonu: </p><p> {adoption.user.address.phone}</p>
                        </div>
                        </div>

                    </>
                ) : (
                    <p>Ładowanie danych zwierzaka...</p>
                )}
                </div>
                <div className='lg:flex justify-center py-10 m-5 md:block'>
                    {isProperShelter && (adoption.adoptionStatus === 'REQUEST_REVIEW' || adoption.status === 'PENDING_SHELTER_INVITED' || adoption.status === 'PENDING') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => inviteUserToShelter(adoption.id)}
                        >
                            Zaproś użytkownika do schroniska
                        </button>
                    )}
                    {isProperShelter && (adoption.adoptionStatus === 'REQUIRES_MANUAL_INVITATION') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => manualInviteUserToShelter(adoption.id)}
                        >
                            Użytkownik zaproszony ręcznie/aktualizacja statusu
                        </button>
                    )}
                    {isProperShelter && (adoption.adoptionStatus === 'SHELTER_INVITED') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => userVisitedShelter(adoption.id)}
                        >
                            Użytkownik odwiedził schronisko, aktualizuje status
                        </button>
                    )}
                    {isProperShelter && (adoption.adoptionStatus === 'VISITED') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => acceptAdoption(adoption.id)}
                        >
                            Zwierzę zaadoptowane, aktualizuje status
                        </button>
                    )}
                    {(isProperShelter || isProperUser) && (adoption.adoptionStatus !== 'DECLINED' && adoption.adoptionStatus !== 'ADOPTED') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => declineAdoption(adoption.id)}
                        >
                            Adopcja odrzucona, aktualizuje status
                        </button>
                    )}
                    {isProperShelter && (adoption.adoptionStatus === 'DECLINED' || adoption.adoptionStatus === 'ADOPTED') && (
                        <button
                            className="bg-orange text-white font-bold py-2 px-4 m-4 rounded"
                            onClick={() => deleteAdoption(adoption.id)}
                        >
                            Usuń adopcje z systemu
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
}
