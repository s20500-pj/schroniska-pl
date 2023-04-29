import React, {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import {AGE_OPTIONS, ANIMAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from '../dog-cat-icon.jpeg';
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";

export default function AnimalDetails() {
    axios.defaults.withCredentials = true;
    const {id} = useParams();
    const [animal, setAnimal] = useState(null);
    const [reload, setReload] = useState(false);
    const [activityFormVisible, setActivityFormVisible] = useState(false);
    const [activityDate, setActivityDate] = useState(null);
    const [activityResponseMessage, setActivityResponseMessage] = useState(null);

    const placeholderImage = icon;
    const onImageError = (e) => {
        e.target.src = placeholderImage
    }

    function canAdopt(animal) {
        const userType = localStorage.getItem("userType");
        const userId = localStorage.getItem("userId");

        if (!isPerson(userType)) {
            return false;
        }

        const hasValidRealAdoption = animal.adoptions.some(
            (adoption) =>
                adoption.user.id === parseInt(userId) &&
                adoption.adoptionType === 'REAL'

            /*&& new Date(adoption.validUntil) >= new Date()*/
        );
        return !hasValidRealAdoption && animal.animalStatus === 'READY_FOR_ADOPTION';
    }

    const entitledForActivity = (animal) => {

        const userType = localStorage.getItem("userType");

        if (!isPerson(userType)) {
            return false;
        }

        const today = new Date();
        const todayNotFree = animal.activities.some(
            activity => {
                const localDateTimeString = activity.activityTime;
                const activityDate = new Date(localDateTimeString);
                return today.getTime() === activityDate.getTime()
            }
        )

        return animal.animalStatus !== 'UNKNOWN' &&
            animal.animalStatus !== 'ADOPTED' &&
            animal.animalStatus !== 'DEAD' &&
            !todayNotFree;

    }

    const isPerson = (userType) => {
        return userType === "PERSON";
    }

    function handleAdoption(animalId) {
        setActivityResponseMessage(false);
        axios
            .post(`http://localhost:8080/adoption/real/${animalId}`)
            .then((response) => {
                alert("Prośba o adopcję wysłana do schroniska!");
                setReload((prevReload) => !prevReload);
            })
            .catch((error) => {
                console.error("Error sending adoption request:", error);
                alert("Error sending adoption request. Please try again.");
            });
    }

    const handleActivity = (e) => {
        e.preventDefault();
        if (activityDate === null){
            alert("Proszę wybrać datę");
            return;
        }
        const activityRegisterReq = {
            animalId: animal.id,
            activityDate: activityDate,
            activityType: "WALKING"
        }
        axios.post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + '/activity/register', JSON.stringify(activityRegisterReq),
            {
                headers: {
                    "Content-Type": ShelterServerConstants.HEADER_APPLICATION_JSON
                }
            })
            .then((response) => {
                setActivityFormVisible(false);
                setActivityResponseMessage(Messages.ACTIVITY_SUCCESS_REGISTRATION + activityDate + ShelterServerConstants.ACTIVITY_TIME);
            })
            .catch((error) => {
                alert(error.response.data);
            });
    }

    const showActivityForm = () => {
        setActivityResponseMessage(false);
        setActivityFormVisible(true);
    }

    useEffect(() => {
        axios.defaults.withCredentials = true;
        axios
            .get(`http://localhost:8080/animal/${id}`)
            .then((response) => setAnimal(response.data))
            .catch((error) => console.error("Error fetching animal data:", error));
    }, [id, reload]);

    const now = new Date();
    if (now.getHours() >= 14) {
        now.setDate(now.getDate() + 1);
    }
    const minDate = now.toISOString().split('T')[0];
    const today = new Date();
    const maxDate = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate() + 1).toISOString().split('T')[0];

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className=" px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-5 h-fit">
                    Szczegóły zwierzaka
                </h2>
                <div className='lg:flex justify-evenly md:block'>
                    {animal ? (
                        <>
                            <div className='px-10 pb-5'>
                                <h2 className=' text-2xl font-bold text-brown'>Cześć jestem</h2>
                                <p className=' text-5xl font-bold text-orange pb-5'>{animal.name}</p>
                                <img src={'/' + animal.imagePath ? '/' + animal.imagePath : placeholderImage}
                                     onError={onImageError}
                                     alt="Zdjęcie zwierzaka"
                                     className="shadow-xl border-2 border-orange rounded-xl object-cover h-[350px] w-[500px]"/>
                            </div>
                            <div className='flex 1/2 lg:pt-24 pb-5 md:justify-center md:p-4'>
                                <div className=''>
                                    <p className='font-bold pt-2'>Gatunek: </p><p>{SPECIES_OPTIONS[animal.species]}</p>
                                    <p className='font-bold pt-2'>Płeć: </p><p>{SEX_OPTIONS[animal.sex]}</p>
                                    <p className='font-bold pt-2'>Wiek: </p><p>{AGE_OPTIONS[animal.age]}</p>
                                    <p className='font-bold pt-2'>Data urodzenia: </p><p>{animal.birthDate}</p>
                                    <p className='font-bold pt-2'>Status:</p>
                                    <p>{ANIMAL_STATUS_OPTIONS[animal.animalStatus]}</p>
                                    <p className='font-bold pt-2'>Dodatkowe informacje:</p><p> {animal.information}</p>
                                </div>
                                <div>
                                    <p className='font-bold pt-2'>Schronisko:</p><p> {animal.shelter.shelterName}</p>
                                    <p className='font-bold pt-2'>
                                        Adres schroniska: </p>
                                    <p>{animal.shelter.address.street}{" "}
                                        {animal.shelter.address.buildingNumber}{" "}
                                        {animal.shelter.address.flatNumber}{" "}
                                        {animal.shelter.address.postalCode}{" "}
                                        {animal.shelter.address.city}
                                    </p>
                                    <p className='font-bold pt-2'>Numer KRS: </p>
                                    <p>{animal.shelter.address.krsNumber}</p>
                                    <p className='font-bold pt-2'>Telefon do schroniska:</p>
                                    <p> {animal.shelter.address.phone}</p>
                                    <div className='flex justify-end py-10'>
                                        {animal && canAdopt(animal) && (
                                            <button
                                                className="bg-orange text-white font-bold py-2 px-4 rounded"
                                                onClick={() => handleAdoption(animal.id)}
                                            >
                                                Adoptuj
                                            </button>
                                        )}
                                        {animal && entitledForActivity(animal) && (
                                            <button
                                                className="bg-orange ml-2 text-white font-bold py-2 px-4 rounded"
                                                onClick={showActivityForm}
                                            >
                                                Wolontariat
                                            </button>
                                        )}
                                        {activityFormVisible && (
                                            <form onSubmit={handleActivity}>
                                                <p>{Messages.ACTIVITY_INFORMATION}</p>
                                                <label>
                                                    Wybierz dzień:
                                                    <input type="date" min={minDate} max={maxDate}
                                                           onChange={(e) => setActivityDate(e.target.value)}/>
                                                </label>
                                                <button type="submit">Zarezerwuj termin</button>
                                            </form>
                                        )}
                                        {activityResponseMessage && <div>{activityResponseMessage}</div>}
                                    </div>

                                </div>
                            </div>

                        </>
                    ) : (
                        <p>Ładowanie danych zwierzaka...</p>
                    )}

                </div>

            </div>
        </div>
    );
}


