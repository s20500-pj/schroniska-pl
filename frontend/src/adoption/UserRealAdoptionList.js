import axios from "axios";
import React, {useState, useEffect, useMemo} from "react";
import Table from "../util/Table";

function UserRealAdoptionList() {
    axios.defaults.withCredentials = true

    const [error, setError] = useState("");

    const SPECIES_OPTIONS = {
        CAT: "Kot",
        DOG: "Pies"
    };

    const AOPTION_TYPE_OPTIONS = {
        REAL: "Prawdziwa",
        VIRTUAL: "Wirtualna"
    };

    const ADOPTION_STATUS_OPTIONS = {
        REQUEST_REVIEW: "Zgłoszenie jest rozpatrywane",
        REQUIRES_MANUAL_INVITATION: "Oczekuje na kontakt ze schroniska",
        PENDING: "Adopcja w toku",
        SHELTER_INVITED: "Zaproszenie do schroniska",
        PENDING_SHELTER_INVITED: "Wizyta w toku",
        VISITED: "Schronisko odwiedzone",
        DECLINED: "Odmowa",
        ADOPTED: "Zwierzę zaadoptowane",
        VIRTUAL_ADOPTED: "Wirtualna adopcja"
    };

    const columns = useMemo(
        () => [
            {
                Header: "Twoje adopcje realne",
                columns: [
                    {
                        Header: "Status adopcji",
                        accessor: "adoptionStatus",
                        Cell: ({value}) => ADOPTION_STATUS_OPTIONS[value]
                    },
                    {
                        Header: "Ważna do:",
                        accessor: "validUntil"
                    }
                ]
            }
        ],
        []
    );

    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const result = await axios.get("http://localhost:8080/adoption/getUserAdoptions/REAL");
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

export default UserRealAdoptionList;