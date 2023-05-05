import React, {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate, useParams} from "react-router-dom";
import {AGE_OPTIONS, ANIMAL_STATUS_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import icon from '../dog-cat-icon.jpeg';
import ShelterServerConstants from "../util/ShelterServerConstants";
import Messages from "../util/Messages";
import VirtualAdoptionBtn from "../adoption/VirtualAdoptionBtn";

export default function AnimalDetails() {
    axios.defaults.withCredentials = true;
    const navigate = useNavigate();
    const {id} = useParams();
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

    const placeholderImage = icon;

    const userType = localStorage.getItem("userType");

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

    function handleSubmit(e) {
        e.preventDefault();
        axios
            .put(`http://localhost:8080/animal/update`, JSON.stringify(animalEdit), {
                withCredentials: true,
                headers: {
                    'Content-type': "application/json"
                }
            })
            .then((response) => {
                setAnimal(response.data);
            })
            .catch((error) => {
                console.error("Error update user data:", error);
            });
    }


    const handleInput = (e) => {
        setAnimalEdit({...animalEdit, [e.target.name]: e.target.value});
        const {name, value} = e.target;
        setAnimalEdit((prevState) => ({
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value,
            },
        }));
    };
    const deleteAnimal = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/animal/delete/${animal.id}`)
            navigate('/shelterAnimalList')
        } catch (error) {
            console.error(error);
        }
    };
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
                                    <div className='block justify-end py-10'>
                                        {animal && canAdopt(animal) && (
                                            <div>
                                                <button
                                                    className="bg-orange text-white font-bold py-2 px-4 rounded m-5"
                                                    onClick={() => handleAdoption(animal.id)}
                                                >
                                                    Adoptuj
                                                </button>
                                            </div>
                                        )}
                                        {animal && entitledForActivity(animal) && (
                                            <div>
                                                <button
                                                    className="bg-orange ml-2 text-white font-bold py-2 px-4 rounded m-5"
                                                    onClick={showActivityForm}
                                                >
                                                    Wolontariat
                                                </button>
                                            </div>
                                        )}
                                        <div>
                                        </div>
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
                                        <VirtualAdoptionBtn isPerson={isPerson} setAnimal={setAnimal} animal={animal}/>
                                    </div>
                                </div>
                                {activityFormVisible && (
                                    <div className="">
                                        <form onSubmit={handleActivity}>
                                            <div className="px-50 ">
                                                <h2 className="text-left text-xl text-orange font-bold h-fit">
                                                    Wolontariat
                                                </h2>
                                                <p className="w-48">{Messages.ACTIVITY_INFORMATION}</p>
                                            </div>
                                            <label>
                                                <p className='font-bold pt-2'>Wybierz dzień: </p>
                                                <input type="date" min={minDate} max={maxDate}
                                                       onChange={(e) => setActivityDate(e.target.value)}/>
                                            </label>
                                            <button
                                                className="bg-orange ml-2 text-white font-bold py-2 px-4 rounded m-5"
                                                type="submit">Zarezerwuj termin
                                            </button>
                                        </form>
                                    </div>)}
                                {activityResponseMessage &&
                                    <div><h2 className="text-left text-xl text-orange font-bold h-fit">
                                        Wolontariat
                                    </h2><p className="w-48 p-4">{activityResponseMessage}</p></div>}
                            </div>
                        </>
                    ) : (
                        <p>Ładowanie danych zwierzaka...</p>
                    )}

                </div>
                {animal && userType === "SHELTER" ? (
                    <div className="text-center">
                        <div>
                            <h2 className=" text-xl text-orange font-bold h-fit pb-5">
                                Poniżej możesz zaktualizować dane.</h2>
                        </div>
                        <div className="flex justify-evenly px-10">
                            <div className="">
                                <button
                                    type="button"
                                    onClick={deleteAnimal}
                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white hover:bg-orange text-white active:bg-brown"
                                >
                                    <p className="py-15 justify-center text-base text-center text-brown font-medium">
                                        Usuń zwierzę
                                    </p></button>
                            </div>
                            <form onSubmit={handleSubmit}>
                                <div className="w-50">
                                    <input
                                        type={"text"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        name="firstName"
                                        defaultValue={animal.name}
                                        onChange={handleInput}
                                    />
                                </div>
                                <div className="">
                                    <label htmlFor="species"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Gatunek:
                                    </label>
                                    <select
                                        className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        name="species"
                                        onChange={handleInput}
                                        defaultValue={SPECIES_OPTIONS[animal.species]}
                                    >
                                        <option value="">Wybierz gatunek</option>
                                        <option value="DOG">Pies</option>
                                        <option value="CAT">Kot</option>
                                    </select>
                                </div>
                                <div className="">
                                    <label htmlFor="sex"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Płeć:
                                    </label>
                                    <select
                                        className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        name="sex"
                                        defaultValue={SEX_OPTIONS[animal.sex]}
                                        onChange={handleInput}
                                    >
                                        <option value="">Wybierz płeć</option>
                                        <option value="MALE">samiec</option>
                                        <option value="FEMALE">samica</option>
                                        <option value="UNKNOWN">nieznany</option>
                                    </select>
                                </div>
                                <div className="">
                                    <label htmlFor="age"
                                           className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Wiek:
                                    </label>
                                    <select
                                        className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        name="age"
                                        defaultValue={AGE_OPTIONS[animal.age]}
                                        onChange={handleInput}
                                    >
                                        <option value="">Wybierz wiek</option>
                                        <option value="VERY_YOUNG">bardzo młody</option>
                                        <option value="YOUNG">młody</option>
                                        <option value="ADULT">dorosły</option>
                                        <option value="ELDER">stary</option>
                                    </select>
                                </div>
                                <div className="">
                                    <label htmlFor="animalStatus"
                                           className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Status zwierzęcia:
                                    </label>
                                    <select
                                        className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        name="animalStatus"
                                        defaultValue={ANIMAL_STATUS_OPTIONS[animal.animalStatus]}
                                        onChange={handleInput}
                                    >
                                        <option value="">Wybierz status</option>
                                        <option value="UNKNOWN">nieznany</option>
                                        <option value="NEEDS_MEDICAL_TREATMENT">potrzebuje opieki medycznej</option>
                                        <option value="READY_FOR_ADOPTION">gotowy do adopcji</option>
                                    </select>
                                </div>
                                <div className="">
                                    <label htmlFor="birthDate"
                                           className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                        Data urodzenia:
                                    </label>
                                    <input
                                        type={"date"}
                                        className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                        placeholder="Data urodzenia"
                                        name="birthDate"
                                        defaultValue={animal.birthDate}
                                        onChange={handleInput}

                                    />
                                </div>
                                <div className="">
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
                                <div className="">
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
                                <div className="">
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
                                <div className="">
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
                                <div className="">
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
                                <div className="">
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
                                <div className="">
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


