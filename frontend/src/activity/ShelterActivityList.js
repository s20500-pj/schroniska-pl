import axios from "axios";
import React, {useEffect, useState} from "react";
import Table from "../util/Table";
import {Link} from "react-router-dom";
import ShelterServerConstants from "../util/ShelterServerConstants";
import {ACTIVITY_TYPE_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import {formatDateWithTime} from "../util/DateUtils";


function ShelterActivityList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);
    const [activity, setActivity] = useState({activityTime: ""});
    const [reload, setReload] = useState(false);
    const [listOfAnimalsWithoutActivity, setListOfAnimalsWithoutActivity] = useState(false);

    const activityMap = new Map(Object.entries(activity)
        .filter(([key, value]) => value !== ""));

    useEffect(() => {
        fetchData();
    }, []);

    const onInputChange = (e) => {
        setActivity({...activity, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const requestData = JSON.stringify(Object.fromEntries(activityMap));

        try {
            if (!listOfAnimalsWithoutActivity) {
                const result = await axios.post(
                    ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/activity/getActivities",
                    requestData, {
                        withCredentials: true,
                        headers: {
                            'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                        }
                    }
                );
                setData(result.data);
            } else {
                const result = await axios.get(
                    ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/activity/getAnimalsNoActivity",
                    {
                        withCredentials: true,
                        headers: {
                            'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                        },
                        params: {
                            date: activity.activityTime
                        }
                    }
                );
                const responseData = result.data.map(animal => {
                    return {
                        animal: {
                            imagePath: animal.imagePath,
                            name: animal.name,
                            species: animal.species,
                            sex: animal.sex,
                            id: animal.id
                        },
                    }
                });
                setData(responseData);
            }
        } catch (error) {
            console.error(error);
        }
    };

    const handleClear = () => {
        if (Object.keys(activity).length === 0) {
            setReload((prevState) => !prevState);
        }
        setActivity({});
    };

    const columns = [
        {
            Header: "Wolontariat",
            columns: [
                {
                    Header: "Zdjęcie",
                    accessor: "animal.imagePath",
                    Cell: ({value}) => <img src={value} alt=""/>
                },
                {
                    Header: "Imię",
                    accessor: "animal.name"
                },
                {
                    Header: "Gatunek",
                    accessor: "animal.species",
                    Cell: ({value}) => SPECIES_OPTIONS[value]
                },
                {
                    Header: "Płeć",
                    accessor: "animal.sex",
                    Cell: ({value}) => SEX_OPTIONS[value]
                },
                {
                    Header: "Rodzaj aktywności",
                    accessor: "activityType",
                    Cell: ({value}) => ACTIVITY_TYPE_OPTIONS[value],
                },
                {
                    Header: "Termin",
                    accessor: "activityTime",
                    Cell: ({value}) => formatDateWithTime(value)
                },
                {
                    Header: "Szczegóły zwierzaka",
                    accessor: "animal.id",
                    Cell: ({value}) => (
                        <Link to={`/animalDetails/${value}`}>
                            <button type="submit"
                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                <p className="py-15 justify-center text-base	 text-center text-brown font-medium	">Zobacz
                                    zwierzę</p>
                            </button>
                        </Link>
                    ),
                },
                {
                    Header: "Rezygnacja",
                    accessor: "id",
                    Cell: ({row}) => (
                        row.original.activityTime !== null && row.original.activityTime !== undefined ?
                            <button type="submit" onClick={() => handleDelete(row.original.id)}
                                    className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                <p className="py-15 justify-center text-base text-center text-brown font-medium	">Anuluj
                                    wolontariat</p>
                            </button> : null
                    ),
                },
            ],
        },
    ];

    const fetchData = async () => {
        try {
            const result = await axios.post(ShelterServerConstants.ADDRESS_SERVER_LOCAL + "/activity/getActivities",
                JSON.stringify(Object.fromEntries(new Map(Object.entries({})))),
                {
                    headers: {
                        'Content-Type': ShelterServerConstants.HEADER_APPLICATION_JSON,
                    }
                });
            setData(result.data)
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = (id) => {
        const confirmed = window.confirm("Czy napewno chcesz zrezygnować ze spotkania z pupilem :( ?");
        if (confirmed) {
            axios.delete(`${ShelterServerConstants.ADDRESS_SERVER_LOCAL}/activity/delete/${id}`)
                .then(response => {
                    if (response.status === 204) {
                        alert("Aktywność anulowana");
                        fetchData();
                    }
                })
                .catch(error => console.error(error));
        }
    }

    return (
        <div className="md:flex p-5 h-fit sm:block sm:h-fit">
            <div className="bg-background-pattern bg-opacity-20 max-w-none md:w-1/4 sm:w-fit sm:h-fit">
                <div className="font-display bg-white bg-opacity-90">
                    <h2 className="text-center text-2xl text-orange font-bold">Filtry</h2>
                    <form onSubmit={(e) => handleSubmit(e)} className="w-full m-auto">
                        <div className="flex flex-wrap p-5">
                            <div className="w-full px-3 mb-6">
                                <label className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Data:
                                </label>
                                <input
                                    className="appearance-none block w-full bg-gray-100 border border-gray-400 hover:border-gray-500 px-4 py-2 rounded leading-tight focus:outline-none focus:bg-white focus:border-gray-500"
                                    type="date" name="activityTime" onChange={(e) => onInputChange(e)}/>
                            </div>
                            <div className="w-full px-3 mb-9">
                                <label className="block uppercase tracking-wide text-brown text-md font-bold">
                                    Pokaż listę zwierząt bez aktywności w danym dniu:
                                </label>
                                <input
                                    className="toggle-checkbox absolute block w-6 h-6 rounded-full border-4 cursor-pointer"
                                    type="checkbox"
                                    name="listOfAnimalsWihtoutActivity"
                                    onChange={(e) => setListOfAnimalsWithoutActivity(e.target.checked)}/>
                            </div>
                            <div className="w-full">
                                <div className="flex justify-around py-2">
                                    <button type="submit"
                                            className="flex px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="justify-center text-base	 text-center text-brown font-medium">Filtruj</p>
                                    </button>
                                    <button onClick={handleClear}
                                            className="px-3 py-2 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                                        <p className="justify-center text-base text-center text-brown font-medium">Czyść
                                            filtry</p>
                                    </button>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>
            <Table columns={columns} data={data} pageSize={20}/>
        </div>
    );
}

export default ShelterActivityList;

