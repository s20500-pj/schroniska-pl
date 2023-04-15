import axios from "axios";
import React, {useState, useEffect, useMemo} from "react";
import Table from "../util/Table";
import AnimalCard from "./AnimalCard";

function ShelterAnimalListt() {
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
        photo: ""
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

    const SPECIES_OPTIONS = {
        CAT: "Kot",
        DOG: "Pies"
    };

    const SEX_OPTIONS = {
        MALE: "Samiec",
        FEMALE: "Samica",
        UNKNOWN: "Nieznany"
    };

    const AGE_OPTIONS = {
        VERY_YOUNG: "Bardzo młody",
        YOUNG: "młody",
        ADULT: "dorosły",
        ELDER: "stary"
    };

    const ANIMAL_STATUS_OPTIONS = {
        UNKNOWN: "nieznany",
        NEEDS_MEDICAL_TREATMENT: "potrzebuje opieki medycznej",
        READY_FOR_ADOPTION: "gotowy do adopcji",
        ADOPTED: "zaadoptowany",
        DEAD: "martwy"
    };

    const columns = useMemo(
        () => [
            {
                Header: "Zwierzęta",
                columns: [
                    {
                        Header: "Zdjęcie",
                        accessor: "imagePath",
                        Cell: ({value}) => <img src={value} alt=""/>
                    },
                    {
                        Header: "Imię",
                        accessor: "name"
                    },
                    {
                        Header: "Gatunek",
                        accessor: "species",
                        Cell: ({value}) => SPECIES_OPTIONS[value]
                    },
                    {
                        Header: "Płeć",
                        accessor: "sex",
                        Cell: ({value}) => SEX_OPTIONS[value]
                    },
                    {
                        Header: "Wiek",
                        accessor: "age",
                        Cell: ({value}) => AGE_OPTIONS[value]
                    },
                    {
                        Header: "Status",
                        accessor: "animalStatus",
                        Cell: ({value}) => ANIMAL_STATUS_OPTIONS[value]
                    },
                ]
            }
        ],
        []
    );


    // data state to store the TV Maze API data. Its initial value is an empty array
    const [data, setData] = useState([]);

    const enteredAnimalFields = Object.fromEntries(
        Object.entries(animal)
            .filter(([_, value]) => value !== "")
            .map(([key, value]) => [`"${key}"`, value])
    );

    const handleSubmit = async (event) => {
        event.preventDefault();
        const result = await axios.post(
            "http://localhost:8080/animal/getShelterAnimals",
            JSON.stringify(enteredAnimalFields), {
                withCredentials: true,
                headers: {
                    'Content-Type': 'text/plain'
                }
            }
        );
        setData(result.data);
    };

    useEffect(() => {
        (async () => {
            const result = await axios.post(
                "http://localhost:8080/animal/getShelterAnimals",
                JSON.stringify(enteredAnimalFields), {
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'text/plain'
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
        <div className="flex p-5">
            <div className="bg-background-pattern bg-opacity-20 max-w-none w-1/3">
                <div className="px-5 font-display bg-white bg-opacity-80">
                    <h2 className="text-center text-2xl text-orange font-bold p-10">Filtry</h2>
                    <form onSubmit={(e) => handleSubmit(e)} className="w-full m-auto py-10">
                        <div className="flex flex-wrap">
                            <div className=" px-3 mb-6 md:mb-0">
                                <label htmlFor="name"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Imię:
                                </label>
                                <input
                                    type={"text"}
                                    className="appearance-none block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    placeholder="Podaj imię"
                                    name="name"
                                    value={name}
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className=" w-full px-3 mb-6 md:mb-0">
                                <label htmlFor="species"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Gatunek:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="species"
                                    value={species}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="DOG">Pies</option>
                                    <option value="CAT">Kot</option>
                                </select>
                            </div>
                            <div className="w-full px-3 mb-6 md:mb-0">
                                <label htmlFor="sex"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Płeć:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                            <div className=" w-full px-3 mb-6 md:mb-0">
                                <label htmlFor="age"
                                       className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Wiek:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="age"
                                    value={age}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="VERY_YOUNG">bardzo młody</option>
                                    <option value="YOUNG">młody</option>
                                    <option value="ADULT">dorosły</option>
                                    <option value="ELDER">stary</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="animalStatus"
                                       className=" block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Status zwierzęcia:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="animalStatus"
                                    value={animalStatus}
                                    defaultChecked={"UNKNOWN"}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value="UNKNOWN">nieznany</option>
                                    <option value="NEEDS_MEDICAL_TREATMENT">potrzebuje opieki medycznej</option>
                                    <option value="READY_FOR_ADOPTION">gotowy do adopcji</option>
                                </select>
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="sterilized"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Wysterylizowany:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                                <label htmlFor="vaccinated"
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Szczepienia:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="vaccinated"
                                    value={vaccinated}
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
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Przyjazny dzieciom:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Kanapowiec:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Potrzebuje dużo ruchu:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Toleruje koty:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
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
                                       className="block uppercase tracking-wide text-brown text-xs font-bold mb-2">
                                    Toleruje psy:
                                </label>
                                <select
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-3 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="dogsFriendly"
                                    value={dogsFriendly}
                                    onChange={(e) => onInputChange(e)}
                                >
                                    <option value="">---</option>
                                    <option value={false}>Nie</option>
                                    <option value={true}>Tak</option>
                                </select>
                            </div>
                            <div className="flex">
                                <button type="submit"
                                        className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                    <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Filtruj</p>
                                </button>
                                <button onClick={handleClear}
                                        className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                    <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Czyść
                                        filtry</p>
                                </button>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
            <AnimalCard data={data}/>
        </div>
    );
}

export default ShelterAnimalListt;