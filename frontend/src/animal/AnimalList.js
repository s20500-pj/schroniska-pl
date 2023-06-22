import axios from "axios";
import React, {useState, useEffect} from "react";
import AnimalCard from "./AnimalCard";
import ShelterServerConstants from "../util/ShelterServerConstants";

function AnimalList() {
    axios.defaults.withCredentials = true;
    const [error, setError] = useState("");
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
        imagePath: ""
    });

    const onInputChange = (e) => {
        setAnimal({...animal, [e.target.name]: e.target.value});
    };

    const {
        name,
        city,
        species,
        sex,
        age,
        animalStatus,
        sterilized,
        vaccinated,
        kidsFriendly,
        couchPotato,
        needsActiveness,
        catsFriendly,
        dogsFriendly
    } = animal;

    const [data, setData] = useState([]);

    const animalMap = new Map(Object.entries(animal)
        .filter(([key, value]) => value !== ""));

    const handleSubmit = async (event) => {
        event.preventDefault();
        const result = await axios.post(
            "http://localhost:8080/animal/search",
            JSON.stringify(Object.fromEntries(animalMap)), {
                withCredentials: true,
                headers: {
                    'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON
                }
            }
        );
        setData(result.data);
    };

    useEffect(() => {
        (async () => {
            const result = await axios.post(
                "http://localhost:8080/animal/search",
                JSON.stringify(Object.fromEntries(animalMap)), {
                    withCredentials: true,
                    headers: {
                        'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON
                    }
                }
            );
            setData(result.data);
        })();
    }, []);

    const handleClear = () => {
        setAnimal({});
    };

    return (
        <div className="md:flex p-5 h-fit sm:block sm:h-fit">
            <div className="bg-background-pattern bg-opacity-20 max-w-none md:w-1/4 sm:w-fit sm:h-fit">
                <div className="px-5 font-display bg-white bg-opacity-90">
                    <h2 className="text-center text-2xl text-orange font-bold p-5">Filtry</h2>
                    <form onSubmit={(e) => handleSubmit(e)} className="w-full m-auto">
                        <div className="flex flex-wrap p-5">
                            <div className=" w-full px-3 ">
                                <label htmlFor="city"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Miasto:
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange  rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Miasto"
                                    name="city"
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className=" w-full px-3 ">
                                <label htmlFor="species"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Gatunek:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="species"
                                    value={species}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="DOG">Pies</option>
                                    <option value="CAT">Kot</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="sex"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Płeć:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="sex"
                                    value={sex}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="MALE">samiec</option>
                                    <option value="FEMALE">samica</option>
                                    <option value="UNKNOWN">nieznany</option>
                                </select>
                            </div>
                            <div className=" w-full px-3 ">
                                <label htmlFor="age"
                                       className=" block uppercase tracking-wide text-brown text-md font-bold">
                                    Wiek:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="age"
                                    value={age}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="VERY_YOUNG">bardzo młody</option>
                                    <option value="YOUNG">młody</option>
                                    <option value="ADULT">dorosły</option>
                                    <option value="ELDER">senior</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="sterilized"
                                       className="block uppercase tracking-wide text-brown text-md font-bold ">
                                    Wysterylizowany:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="sterilized"
                                    value={sterilized}
                                    defaultChecked={false}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="kidsFriendly"
                                       className="block uppercase tracking-wide text-brown text-md font-bold ">
                                    Przyjazny dzieciom:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="kidsFriendly"
                                    value={kidsFriendly}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="couchPotato"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Kanapowiec:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="couchPotato"
                                    value={couchPotato}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="needsActiveness"
                                       className="block uppercase tracking-wide text-brown text-md font-bold ">
                                    Potrzebuje dużo ruchu:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="needsActiveness"
                                    value={needsActiveness}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="catsFriendly"
                                       className="block uppercase tracking-wide text-brown text-md font-bold ">
                                    Toleruje koty:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="catsFriendly"
                                    value={catsFriendly}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="dogsFriendly"
                                       className="block uppercase tracking-wide text-brown text-md font-bold ">
                                    Toleruje psy:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="dogsFriendly"
                                    value={dogsFriendly}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="w-full">
                                <div className="flex justify-around py-2">
                                    <button type="submit"
                                            className="flex px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className=" justify-center text-base	 text-center text-brown font-medium	">Filtruj</p>
                                    </button>
                                    <button onClick={handleClear}
                                            className="px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="justify-center text-base	 text-center text-brown font-medium	">Czyść
                                            filtry</p>
                                    </button>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>

            <AnimalCard data={data}/>
        </div>
    );

}

export default AnimalList;