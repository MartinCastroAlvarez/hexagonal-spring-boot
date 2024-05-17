// Utils.tsx

import React, { useState, useCallback, useEffect } from 'react';

import { toast } from 'react-toastify';
import {
    faStar,
    faKey,
    faFile,
    faX,
    faClock,
    faCheck,
    faEnvelope,
    faCalendarDays
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const ROLES = {
    ADMIN: 'ADMIN',
    USER: 'USER',
    MANAGER: 'MANAGER',
    SALESMAN: 'SALESMAN',
    PROVIDER: 'PROVIDER',
}

export const DAY_OF_WEEK = {
    SUNDAY: 'SUNDAY',
    MONDAY: 'MONDAY',
    TUESDAY: 'TUESDAY',
    WEDNESDAY: 'WEDNESDAY',
    THURSDAY: 'THURSDAY',
    FRIDAY: 'FRIDAY',
    SATURDAY: 'SATURDAY',
}

export const PTO_TYPE = {
    VACATION: 'VACATION',
    SICK: 'SICK',
    PERSONAL: 'PERSONAL',
}

export const notifyError = (error: String) => {
    toast.error(error, {
        position: "bottom-right"
    });
}

export const notifyMessage = (message: String) => {
    toast.info(message, {
        position: "bottom-right"
    });
}

export const String = (value: any) => {
    value = value.value;
    if (value === null || value === undefined)
        return <></>;
    if (typeof value === 'boolean') {
        return <>
            <FontAwesomeIcon icon={value ? faCheck : faX} />
        </>
    }
    if (typeof value !== 'string')
        value = value.toString();
    if (value == ROLES.ADMIN)
        return <>
            <FontAwesomeIcon icon={faStar} />
            &nbsp;
            {value.toString()}
        </>
    if (Object.values(ROLES).includes(value))
        return <>
            <FontAwesomeIcon icon={faKey} />
            &nbsp;
            {value.toString()}
        </>
    if (Object.values(PTO_TYPE).includes(value))
        return <>
            <FontAwesomeIcon icon={faCalendarDays} />
            &nbsp;
            {value.toString()}
        </>
    if (Object.values(DAY_OF_WEEK).includes(value))
        return <>
            <FontAwesomeIcon icon={faCalendarDays} />
            &nbsp;
            {value.toString()}
        </>
    const matchEmail = value.match(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/);
    if (matchEmail)
        return <>
            <FontAwesomeIcon icon={faEnvelope} />
            &nbsp;
            {value.toString()}
        </>
    const matchFile = value.match(/^[a-zA-Z0-9._%+-]+\.[a-zA-Z]{3,4}$/);
    if (matchFile)
        return <>
            <FontAwesomeIcon icon={faFile} />
            &nbsp;
            {value.toString()}
        </>
    const matchDate = value.match(/^(\d{4})-(\d{2})-(\d{2})$/);
    if (matchDate) {
        const [, year, month, day] = matchDate;
        const date = new Date(
            parseInt(year),
            parseInt(month) - 1,
            parseInt(day),
        );
        return <>
            <FontAwesomeIcon icon={faCalendarDays} />
            &nbsp;
            {date.toISOString().replace('T', ' ').slice(0, 10).toString()}
        </>
    }
    const matchTime = value.match(/^(\d{2}):(\d{2}):(\d{2})$/);
    if (matchTime) {
        const [, hours, minutes, seconds] = matchTime;
        return <>
            <FontAwesomeIcon icon={faClock} />
            &nbsp;
            {hours}:{minutes}:{seconds}
        </>
    }
    const matchDateTime = value.match(/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}).\d+$/);
    if (matchDateTime) {
        const [, year, month, day, hours, minutes, seconds] = matchDateTime;
        const date = new Date(
            parseInt(year),
            parseInt(month) - 1,
            parseInt(day),
            parseInt(hours),
            parseInt(minutes),
            parseInt(seconds)
        );
        return <>
            <FontAwesomeIcon icon={faCalendarDays} />
            &nbsp;
            {date.toISOString().replace('T', ' ').slice(0, -5).toString()}
        </>
    }
    return <>{value.toString()}</>;
}

export const isSessionAuthenticated = (session: any) : boolean => {
    console.log(`Utils.isSessionAuthenticated: ${JSON.stringify(session)}`);
    return session.id && session.name && session.role;
}

export const useLoader = (asyncFunction: any, dependencies: any[]) : [any, boolean, any] => {
    const [data, setData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const load = useCallback(async () => {
        console.log(`Utils.useLoader.load: ${asyncFunction.name}()`);
        setIsLoading(true);
        const response: any = await asyncFunction();
        setIsLoading(false);
        setData(response)
    }, [asyncFunction]);
    useEffect(() => {
        let isMounted = true;
        load().then(() => {
            if (!isMounted) return;
        });
        return () => {
            isMounted = false;
        };
    }, dependencies)
    return [data, isLoading, load];
}

export const getMonthsAgo = (months: number) => {
  const date = new Date();
  date.setMonth(date.getMonth() - months);
  return date;
};

export const getMonthsAhead = (months: number) => {
  const date = new Date();
  date.setMonth(date.getMonth() + months);
  return date;
};

export const usePagination = () => {
    const [page, setPage] = useState(0);
    const [query, setQuery] = useState('');
    const [size, setSize] = useState(10);
    const [asc, setAsc] = useState(true);
    const [sort, setSort] = useState('id');
    return {
        page,
        setPage,
        query,
        setQuery,
        size,
        setSize,
        asc,
        setAsc,
        sort,
        setSort,
    }
}