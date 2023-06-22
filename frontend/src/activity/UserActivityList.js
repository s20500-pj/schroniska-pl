import axios from "axios";
import React, {useEffect, useState} from "react";
import Table from "../util/Table";
import ShelterServerConstants from "../util/ShelterServerConstants";
import {ACTIVITY_TYPE_OPTIONS, SEX_OPTIONS, SPECIES_OPTIONS} from "../util/Enums";
import {formatDateWithTime} from "../util/DateUtils";

function UserActivityList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);

    useEffect(() => {
        fetchData();
    }, []);

    const columns = [
        {
            Header: "Twoje wolontariaty",
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
                    Header: "Nazwa schroniska",
                    accessor: "animal.shelter.shelterName"
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
                    Header: "",
                    accessor: "id",
                    Cell: ({row}) => (
                        <button type="submit" onClick={() => handleDelete(row.original.id)}
                                className="px-10 py-2 m-5 border-2 border-orange rounded-2xl bg-white  hover:bg-orange text-white active:bg-brown ">
                            <p className="py-15 justify-center text-base text-center text-brown font-medium	">Zrezygnuj</p>
                        </button>
                    ),
                }
            ],
        },
    ];

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

    return (
        <div className="App">
            {data && <Table columns={columns} data={data} pageSize={20}/>}
        </div>
    );
}

export default UserActivityList;