import { useState, useEffect } from 'react'

const useAPI = (term) => {
    const [data, setData] = useState(null);
    useEffect(() => {
        const fetchData = async() => {
            fetch(
                `/${term}`
            )
            .then(response => response.json())
            .then(result => {setData(result)})
        }

        fetchData();

    }, [term])
    return { data }
};

export default useAPI
