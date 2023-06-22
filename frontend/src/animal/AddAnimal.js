import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import PopupSuccessAdd from "./PopupSuccessAdd";

export default function AddAnimal() {
    axios.defaults.withCredentials = true
    const [modalOpen, setModalOpen] = useState(false);
    const [error, setError] = useState("");
    let navigate = useNavigate();
    const [animal, setAnimal] = useState({
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

    const [photo, setPhoto] = useState(null);
    const currentDate = new Date().toISOString().slice(0, 10);
    const handlePhotoChange = (event) => {
        const selectedFile = event.target.files[0];
        setPhoto(selectedFile);
    };

    const onInputChange = (e) => {
        setAnimal({...animal, [e.target.name]: e.target.value});
    };

    const handleCancelClick = () => {
        navigate("/");
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        const json = JSON.stringify(animal);
        const blob = new Blob([json], {
            type: 'application/json'
        });
        formData.append('animal', blob);
        formData.append("image", photo);
        try {
            const response = await axios.post(
                "http://localhost:8080/animal/add",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );
            setModalOpen(true);
        } catch (error) {
            setError(error.response?.data?.message || "Something went wrong");
        }
    };

    const {
        name,
        information,
        species,
        sex,
        age,
        birthDate,
        animalStatus,
        sterilized,
        vaccinated,
        kidsFriendly,
        couchPotato,
        needsActiveness,
        catsFriendly,
        dogsFriendly,
    } = animal;

    return (
        <div className="bg-background-pattern bg-opacity-20 max-w-none">
            <div className="px-10 font-display bg-white bg-opacity-90">
                <h2 className="text-center text-2xl text-orange font-bold p-10">Dodaj zwięrzę do schroniska</h2>
                {modalOpen && <PopupSuccessAdd setOpenModal={setModalOpen}/>}
                <form onSubmit={(e) => onSubmit(e)} className="w-full max-w-lg m-auto pb-5">
                    <div className="flex flex-wrap">
                        <div className="md:w-1/2 px-3 mb-6 md:mb-0">
                            <label htmlFor="name"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Imię:
                            </label>
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Podaj imię."
                                name="name"
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="information"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Informacje/opis:
                            </label>
                            <input
                                type={"text"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Informacje/opis"
                                name="information"
                                onChange={(e) => onInputChange(e)}
                                required
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="species"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Gatunek:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="species"
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz gatunek</option>
                                <option value="DOG">Pies</option>
                                <option value="CAT">Kot</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="sex"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Płeć:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="sex"
                                value={sex}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz płeć</option>
                                <option value="MALE">samiec</option>
                                <option value="FEMALE">samica</option>
                                <option value="UNKNOWN">nieznany</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="age"
                                   className=" block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Wiek:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="age"
                                value={age}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz wiek</option>
                                <option value="VERY_YOUNG">bardzo młody</option>
                                <option value="YOUNG">młody</option>
                                <option value="ADULT">dorosły</option>
                                <option value="ELDER">senior</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="animalStatus"
                                   className=" block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Status zwierzęcia:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="animalStatus"
                                value={animalStatus}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz status</option>
                                <option value="UNKNOWN">nieznany</option>
                                <option value="NEEDS_MEDICAL_TREATMENT">potrzebuje opieki medycznej</option>
                                <option value="READY_FOR_ADOPTION">gotowy do adopcji</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="birthDate"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Data urodzenia:
                            </label>
                            <input
                                type={"date"}
                                className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                placeholder="Data urodzenia"
                                name="birthDate"
                                value={birthDate}
                                onChange={(e) => onInputChange(e)}
                                required
                                max={currentDate}
                            />
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="sterilized"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Wysterylizowany?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="sterilized"
                                value={sterilized}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wysterylizowany?</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="vaccinated"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Szczepienia?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="vaccinated"
                                value={vaccinated}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Szczepiony?</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="kidsFriendly"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Przyjazny dzieciom?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="kidsFriendly"
                                value={kidsFriendly}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz z listy</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="couchPotato"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Kanapowiec?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="couchPotato"
                                value={couchPotato}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz z listy</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="needsActiveness"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Potrzebuje dużo ruchu?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="needsActiveness"
                                value={needsActiveness}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz z listy</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="catsFriendly"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Toleruje koty?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="catsFriendly"
                                value={catsFriendly}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz z listy</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="dogsFriendly"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Toleruje psy?:
                            </label>
                            <select
                                className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                name="dogsFriendly"
                                value={dogsFriendly}
                                onChange={(e) => onInputChange(e)}
                                required
                            >
                                <option value="">Wybierz z listy</option>
                                <option value={false}>Nie</option>
                                <option value={true}>Tak</option>
                            </select>
                        </div>
                        <div className="w-full md:w-1/2 px-3">
                            <label htmlFor="photo"
                                   className="block uppercase tracking-wide text-brown text-s font-bold mb-2">
                                Zdjęcie:
                            </label>
                            {/*<input type="file" name="photo" onChange={handlePhotoChange}/>*/}
                            {photo && (
                                <p
                                    className="block uppercase tracking-wide text-orange text-md font-bold mb-2">
                                    Wybrano plik: {photo.name}</p>
                            )}
                            <input type="file" name="photo"
                                   className="block w-50 text-sm text-gray-500
                                    file:mr-4 file:py-2 file:px-4
                                    file:rounded-full file:border-0
                                    file:text-sm file:font-semibold
                                    file:text-brown file:border-1 file:border-orange
                                    hover:file:bg-orange"
                                   onChange={handlePhotoChange}/>

                        </div>


                    </div>
                    <button type="submit"
                            className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                        <p className="py-15 justify-center text-base text-center text-brown font-medium	">Zarejestruj</p>
                    </button>
                    <button onClick={handleCancelClick} type="submit"
                            className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                        <a
                            href="/"
                            className='py-15 justify-center text-base text-center text-brown font-medium	'
                        >
                            Anuluj </a>
                    </button>
                </form>
            </div>
        </div>
    )
}