import { createContext, useState } from 'react';
import PropTypes from 'prop-types';

const DataContext = createContext(undefined);

export const DataProvider = ({ children }) => {
    const [sharedData, setSharedData] = useState({});

    const updateData = (data) => {
        setSharedData(data);
    };

    return (
        <DataContext.Provider value={{ sharedData, updateData }}>
            {children}
        </DataContext.Provider>
    );
};

DataProvider.propTypes = {
    children: PropTypes.node.isRequired
};
