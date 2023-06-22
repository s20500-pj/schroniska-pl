import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {AGE_OPTIONS, ANIMAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from '../dog-cat-icon.jpeg';
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";
import VirtualAdoptionBtn from "../adoption/VirtualAdoptionBtn";
import {arrayToDate, formatDate} from "../util/DateUtils";
import PopupSuccessActivity from "../activity/PopupSuccessActitity";
import PopupDeleteUserByUser from "../users/person/PopupDeleteUserByUser";
import PopupDeleteAnimal from "./PopupDeleteAnimal";

export default function AnimalDetails() {
    axios.defaults.withCredentials = true;
    const navigate = useNavigate();
    const {id} = useParams();
    const [isProperShelter, setIsProperShelter] = useState(false);
    const [animal, setAnimal] = useState(null);
    const [animalEdit, setAnimalEdit] = useState({
        id: id,
        name: "",
        information: "",
        species: "",
        sex: "",
        age: "",
        birthDate: "",
        animalStatus: "",
        sterilized: "",
        vaccinated: "",
        kidsFriendly: "",
        couchPotato: "",
        needsActiveness: "",
        catsFriendly: "",
        dogsFriendly: "",
    });
    const [reload, setReload] = useState(false);
    const [activityFormVisible, setActivityFormVisible] = useState(false);
    const [activityDate, setActivityDate] = useState(null);
    const [activityResponseMessage, setActivityResponseMessage] = useState(null);
    const [modalOpen, setModalOpen] = useState(false);
    const placeholderImage = icon;
    const userType = localStorage.getItem("userType");

    const onImageError = (e) => {
        e.target.src = placeholderImage
    }

    const [photo, setPhoto] = useState(null);

    const handlePhotoChange = (event) => {
        const selectedFile = event.target.files[0];
        setPhoto(selectedFile);
    };

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

        if (activityDate === null) {
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
                setModalOpen(true);
                // setActivityResponseMessage(Messages.ACTIVITY_SUCCESS_REGISTRATION + formatDate(activityDate) + ' ' + ShelterServerConstants.ACTIVITY_TIME);
            })
            .catch((error) => {
                alert(error.response.data);
            });
    }

    const showActivityForm = () => {
        setActivityResponseMessage(false);
        setActivityFormVisible(true);
    }

    const closeActivityForm = () => {
        setActivityFormVisible(true);
    }

    useEffect(() => {
        axios.defaults.withCredentials = true;
        axios
            .get(`http://localhost:8080/animal/${id}`)
            .then((response) => setAnimal(response.data))
            .catch((error) => console.error("Error fetching animal data:", error));
    }, [id, reload]);

    useEffect(() => {
        if (animal) {
            setIsProperShelter(
                localStorage.getItem("userType") === "SHELTER" &&
                localStorage.getItem("userId") == animal.shelter.id
            );
        }
    }, [animal]);

    const now = new Date();
    if (now.getHours() >= 14) {
        now.setDate(now.getDate() + 1);
    }
    const minDate = now.toISOString().split('T')[0];
    const today = new Date();
    const maxDate = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate() + 1).toISOString().split('T')[0];

    function handleSubmit(e) {
        e.preventDefault();
        const formData = new FormData();
        const updatedAnimal = filterEmptyAndUnchanged(animalEdit, animal);
        const json = JSON.stringify(updatedAnimal);
        const blob = new Blob([json], {
            type: 'application/json'
        });
        formData.append('animal', blob);
        formData.append("image", photo);

        axios
            .put(
                "http://localhost:8080/animal/update",
                formData,
                {
                    headers: {
                        "Content-type": "multipart/form-data",
                    },
                }
            )
            .then((response) => {
                setAnimal(response.data);
            });
    }

    const handleInput = (e) => {
        const {name, value} = e.target;
        setAnimalEdit((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    function filterEmptyAndUnchanged(animalEdit, originalAnimal) {
        return Object.entries(animalEdit).reduce((filtered, [key, value]) => {
            if (value !== "" && value !== originalAnimal[key]) {
                filtered[key] = value;
            }
            return filtered;
        }, {});
    }

    const deleteAnimal = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/animal/delete/${animal.id}`)
            navigate('/shelterAnimalList')
        } catch (error) {
            console.error(error);
        }
    };
    const userId = localStorage.getItem("userId");

    const alreadyVirtuallyAdopted = (animal) => {
        return animal.adoptions.some(
            (adoption) =>
                adoption.user.id === parseInt(userId) &&
                adoption.adoptionType === 'VIRTUAL'
        );
    }
    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className=" px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-5 h-fit">
                    Szczegóły zwierzaka
                </h2>   {animal ? (
                <>
                    <div className='lg:flex justify-evenly md:block'>

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
                                <p className='font-bold pt-2 text-brown'>Gatunek: </p><p>{SPECIES_OPTIONS[animal.species]}</p>
                                <p className='font-bold pt-2 text-brown'>Płeć: </p><p>{SEX_OPTIONS[animal.sex]}</p>
                                <p className='font-bold pt-2 text-brown'>Wiek: </p><p>{AGE_OPTIONS[animal.age]}</p>
                                <p className='font-bold pt-2 text-brown'>Data urodzenia: </p>
                                <p>{formatDate(animal.birthDate)}</p>
                                <p className='font-bold pt-2 text-brown'>Status:</p>
                                <p>{ANIMAL_STATUS_OPTIONS[animal.animalStatus]}</p>
                                <p className='font-bold pt-2 text-brown'>Dodatkowe informacje:</p><p> {animal.information}</p>
                            </div>
                            <div>
                                <p className='font-bold pt-2 text-brown'>Schronisko:</p><p> {animal.shelter.shelterName}</p>
                                <p className='font-bold pt-2 text-brown'>
                                    Adres schroniska: </p>
                                <p>{animal.shelter.address.street}{" "}
                                    {animal.shelter.address.buildingNumber}{" "}
                                    {animal.shelter.address.flatNumber}{" "}
                                    {animal.shelter.address.postalCode.slice(0, 2)}-{animal.shelter.address.postalCode.slice(2)}{" "}
                                    {animal.shelter.address.city}
                                </p>
                                <p className='font-bold pt-2 text-brown'>Numer KRS: </p>
                                <p>{animal.shelter.address.krsNumber}</p>
                                <p className='font-bold pt-2 text-brown'>Telefon do schroniska:</p>
                                <p> {animal.shelter.address.phone}</p>
                                <p className='font-bold pt-2 text-brown'>E-mail:</p>
                                <p> {animal.shelter.email}</p>
                            </div>
                        </div>


                    </div>
                    <div className='flex justify-center py-10'>
                        {animal && canAdopt(animal) && (
                            <div>
                                <button
                                    className="ml-2 text-brown font-bold py-2 px-4 m-5
                                     bg-white border-orange text-brown hover:bg-orange border-2 rounded-2xl m-3 p-3 hover:scale-105"
                                    onClick={() => handleAdoption(animal.id)}
                                >
                                    Adoptuj
                                </button>
                            </div>
                        )}
                        {animal && entitledForActivity(animal) && (
                            <div>
                                <button
                                    className=" ml-2 text-brown font-bold py-2 px-4 m-5
                                     bg-white border-orange text-brown hover:bg-orange border-2 rounded-2xl m-3 p-3 hover:scale-105"
                                    onClick={showActivityForm}
                                >
                                    Wolontariat
                                </button>
                            </div>
                        )}
                        {activityFormVisible && (
                            <form onSubmit={handleActivity}>
                                 <div className="justify-center items-center flex overflow-x-hidden overflow-y-auto fixed inset-0 z-50">
                                <div className="relative w-auto my-6 mx-auto max-w-3xl">
                                    <div
                                        className="p-4 border-0 rounded-lg shadow-lg relative flex flex-col w-full bg-white outline-none focus:outline-none">
                                       <button onClick={()=>setActivityFormVisible(false)}>
                                           <h2 className="text-right text-orange">X</h2>
                                       </button>
                                        <h2 className="text-center text-xl text-orange font-bold h-fit mb-3">
                                            Wolontariat
                                        </h2>
                                        <p className="">{Messages.ACTIVITY_INFORMATION}</p>
                                        <label className="flex justify-center p-6 focus:outline-orange">
                                            <p className="font-bold text-orange">Wybierz dzień: </p>
                                            <input type="date" min={minDate} max={maxDate}
                                                   onChange={(e) => setActivityDate(e.target.value)}
                                                   className="mx-2"/>
                                        </label>
                                        <button
                                            className="bg-orange ml-2 text-white  py-2 px-4 rounded-2xl m-5 hover:scale-103"
                                            type="submit">
                                            <p>Zarezerwuj termin</p>
                                        </button>
                                    </div>
                                </div>
                            </div>
                            </form>
                        )}
                        <div>
                            {modalOpen && <PopupSuccessActivity setOpenModal={setModalOpen}/>}
                            {activityResponseMessage && <div>{activityResponseMessage}</div>}
                            {alreadyVirtuallyAdopted(animal) ? (
                                    <div>
                                        <p className=" pt-6 text-orange font-bold">Adoptowałeś już to zwierzę wirtualnie</p>
                                    </div>) :
                                (<VirtualAdoptionBtn isPerson={isPerson} animal={animal}/>)}
                        </div>
                    </div>
                </>
            ) : (
                <p>Ładowanie danych zwierzaka...</p>
            )}


                {animal && userType === "SHELTER" ? (
                    <div className="text-center">
                        <div>
                            <div className="">
                                {modalOpen && <PopupDeleteAnimal setOpenModal={setModalOpen} navigate={navigate} id={id} animal={animal}/>}
                                <button
                                    type="button"
                                    onClick={() => setModalOpen(true)}
                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown"
                                >
                                    <p className="py-15 justify-center text-base text-center text-brown font-medium">
                                        Usuń zwierzę
                                    </p></button>
                            </div>
                            <h2 className=" text-xl text-orange font-bold h-fit pb-5">
                                Poniżej możesz zaktualizować dane.</h2>
                        </div>
                        <div className="flex justify-evenly px-10">

                            <form onSubmit={handleSubmit}>
                                <div className="flex flex-wrap">
                                    <div className="w-50 p-6">
                                        <label htmlFor="species"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Imię:
                                        </label>
                                        <input
                                            type={"text"}
                                            className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="name"
                                            defaultValue={animal.name}
                                            onChange={handleInput}
                                        />
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="species"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Informacje/opis:
                                        </label>
                                        <input
                                            type={"text"}
                                            className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="information"
                                            defaultValue={animal.information}
                                            onChange={handleInput}
                                        />
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="species"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Gatunek:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="species"
                                            onChange={handleInput}
                                            defaultValue={animal.species}
                                        >
                                            <option value="">Wybierz gatunek</option>
                                            <option value="DOG">Pies</option>
                                            <option value="CAT">Kot</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="sex"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Płeć:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="sex"
                                            defaultValue={animal.sex}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz płeć</option>
                                            <option value="MALE">samiec</option>
                                            <option value="FEMALE">samica</option>
                                            <option value="UNKNOWN">nieznany</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="age"
                                               className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Wiek:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="age"
                                            defaultValue={animal.age}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz wiek</option>
                                            <option value="VERY_YOUNG">bardzo młody</option>
                                            <option value="YOUNG">młody</option>
                                            <option value="ADULT">dorosły</option>
                                            <option value="ELDER">stary</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="animalStatus"
                                               className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Status zwierzęcia:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="animalStatus"
                                            defaultValue={animal.animalStatus}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz status</option>
                                            <option value="UNKNOWN">nieznany</option>
                                            <option value="NEEDS_MEDICAL_TREATMENT">potrzebuje opieki medycznej</option>
                                            <option value="READY_FOR_ADOPTION">gotowy do adopcji</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="birthDate"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Data urodzenia:
                                        </label>
                                        <input
                                            type={"date"}
                                            className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            placeholder="Data urodzenia"
                                            name="birthDate"
                                            defaultValue={arrayToDate(animal.birthDate)}
                                            onChange={handleInput}
                                        />
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="sterilized"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Wysterylizowany?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="sterilized"
                                            defaultValue={animal.sterilized}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wysterylizowany?</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="vaccinated"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Szczepienia?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="vaccinated"
                                            defaultValue={animal.vaccinated}
                                            onChange={handleInput}
                                        >
                                            <option value="">Szczepiony?</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="kidsFriendly"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Przyjazny dzieciom?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="kidsFriendly"
                                            defaultValue={animal.kidsFriendly}
                                            onChange={handleInput}

                                        >
                                            <option value="">Wybierz z listy</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="couchPotato"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Kanapowiec?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="couchPotato"
                                            defaultValue={animal.couchPotato}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz z listy</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="needsActiveness"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Potrzebuje dużo ruchu?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="needsActiveness"
                                            defaultValue={animal.needsActiveness}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz z listy</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="catsFriendly"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Toleruje koty?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="catsFriendly"
                                            defaultValue={animal.catsFriendly}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz z listy</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="dogsFriendly"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Toleruje psy?:
                                        </label>
                                        <select
                                            className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                            name="dogsFriendly"
                                            defaultValue={animal.dogsFriendly}
                                            onChange={handleInput}
                                        >
                                            <option value="">Wybierz z listy</option>
                                            <option value={false}>Nie</option>
                                            <option value={true}>Tak</option>
                                        </select>
                                    </div>
                                    <div className="w-50 p-6">
                                        <label htmlFor="photo"
                                               className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                            Zdjęcie:
                                        </label>
                                        <input type="file" name="photo"
                                               className="block w-50 text-sm text-gray-500
                                    file:mr-4 file:py-2 file:px-4
                                    file:rounded-full file:border-0
                                    file:text-sm file:font-semibold
                                    file:text-brown file:border-1 file:border-orange
                                    hover:file:bg-orange"type="file" name="photo" onChange={handlePhotoChange}/>
                                        {photo && (
                                            <p>Wybrano plik: {photo.name}</p>
                                        )}


                                    </div>
                                </div>
                                <div className="m-auto text-center">
                                    <button type="submit"
                                            className=" px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="py-15 justify-center text-base text-center text-brown font-medium	">Aktualizuj
                                            dane</p>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                ) : (<></>)}
            </div>
        </div>
    );
}


