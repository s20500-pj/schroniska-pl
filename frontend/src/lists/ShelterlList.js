import axios from "axios";
import React, {useState, useEffect} from "react";
import {Link} from "react-router-dom";
import Table from "../util/Table";

const columns = [
    {
        Header: "Lista schronisk",
        columns: [
            {
                Header: "Nazwa schroniska",
                accessor: "shelterName"
            },
            {
                Header: "Email",
                accessor: "email"
            },
            {
                Header: "Miasto",
                accessor: "address.city"
            },
            {
                Header: "Telefon",
                accessor: "address.phone"
            },
            {
                Header: "KRS",
                accessor: "address.krsNumber"
            },
            {
                Header: "Szczegóły",
                accessor: "id",
                Cell: ({value}) => (
                    <Link to={`/shelterDetails/${value}`}>
                        Zobacz szczegóły/ do implementacji
                    </Link>
                ),
            }
        ],
    },
];

function ShelterList() {
    axios.defaults.withCredentials = true
    const [error, setError] = useState("");
    const [data, setData] = useState([]);
    const [shelter, setShelter] = useState({
        shelterName: "",
        email: "",
        information: "",
        city: "",
        street: ""
    });

    const onInputChange = (e) => {
        setShelter({...shelter, [e.target.name]: e.target.value});
    };

    const {
        shelterName,
        email,
        information,
        city,
        street
    } = shelter;

    const enteredShelterFields = {
        ...Object.fromEntries(
            Object.entries(shelter)
                .filter(([_, value]) => value !== "")
                .map(([key, value]) => [`"${key}"`, value])
        ),
        "\"userType\"": "SHELTER"
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const result = await axios.post(
            "http://localhost:8080/shelter/searchShelters",
            JSON.stringify(enteredShelterFields), {
                withCredentials: true,
                headers: {
                    'Content-Type': 'text/plain'
                }
            }
        );
        setData(result.data);
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.post(
                    "http://localhost:8080/shelter/searchShelters",
                    JSON.stringify(enteredShelterFields), {
                        withCredentials: true,
                        headers: {
                            'Content-Type': 'text/plain'
                        }
                    }
                );
                setData(result.data);
            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);

    const handleClear = () => {
        setShelter({});
    };

    return (
        <div className="md:flex p-5 h-fit sm:block sm:h-fit">
            <div className="bg-background-pattern bg-opacity-20 max-w-none md:w-1/4 sm:w-fit sm:h-fit">
                <div className="px-5 font-display bg-white bg-opacity-90">
                    <h2 className="text-center text-2xl text-orange font-bold p-5">Filtry</h2>
                    <form onSubmit={(e) => handleSubmit(e)} className="w-full m-auto">
                        <div className="flex flex-wrap p-5">
                            <div className="w-full px-3">
                                <label htmlFor="shelterName"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Nazwa schroniska:
                                </label>
                                <input
                                    type="text"
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="shelterName"
                                    value={shelterName}
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="email"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    E-mail:
                                </label>
                                <input
                                    type="text"
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="email"
                                    value={email}
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="city"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Miasto:
                                </label>
                                <input
                                    type="text"
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="city"
                                    value={city}
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className="w-full px-3">
                                <label htmlFor="street"
                                       className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Ulica:
                                </label>
                                <input
                                    type="text"
                                    className="block w-full bg-gray-200 text-brown border border-orange rounded py-2 px-4 mb-3 leading-tight focus:outline-none focus:bg-white"
                                    name="street"
                                    value={street}
                                    onChange={(e) => onInputChange(e)}
                                />
                            </div>
                            <div className="w-full px-3">
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

            <Table columns={columns} data={data}/>
        </div>
    );
}

export default ShelterList;