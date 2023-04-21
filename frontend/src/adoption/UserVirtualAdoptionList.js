import axios from "axios";
import React, {useState, useEffect} from "react";
import Table from "../util/Table";
import {ADOPTION_STATUS_OPTIONS} from "../util/Enums";

const columns = [
    {
        Header: "Twoje adopcje wirtualne",
        columns: [
            {
                Header: "Status adopcji",
                accessor: "adoptionStatus",
                Cell: ({value}) => ADOPTION_STATUS_OPTIONS[value],
            },
            {
                Header: "Ważna do:",
                accessor: "validUntil",
            },
        ],
    },
];

function UserVirtualAdoptionList() {
    axios.defaults.withCredentials = true;

    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.get("http://localhost:8080/adoption/getUserAdoptions/VIRTUAL");
                setData(result.data);
            } catch (error) {
                console.error(error);
            }
        };
        fetchData();
    }, []);

    return (
        <div className="App">
            <Table columns={columns} data={data} pageSize={20}/>
        </div>
    );
}

export default UserVirtualAdoptionList;
