// Pages.tsx

import React, { useState } from 'react';

import { useSelector, useDispatch } from 'react-redux';
import { useParams, useNavigate } from 'react-router-dom';
import { useLoader, getMonthsAgo, getMonthsAhead, usePagination } from './Utils';

import {
  HexProductListForm,
  HexSaleListForm,
  HexPurchaseListForm,
  HexMessageListForm,
  HexMeetingListForm,
  HexPtoListForm,
  HexScheduleListForm,
  HexWorkForm,
  HexUserDetailsForm,
  HexProductDetailsForm,
  HexMeetingDetailsForm,
  HexMessageDetailsForm,
  HexPtoDetailsForm,
  HexScheduleDetailsForm,
  HexCreatePurchaseForm,
  HexCreateProductForm,
  HexCreateSaleForm,
  HexCreateMeetingForm,
  HexInviteUserToMeetingForm,
  HexCreateMessageForm,
  HexCreateScheduleForm,
  HexCreatePtoForm,
  HexUpdateUserForm,
  HexUpdateProductForm,
  HexUploadFileForm,
} from './Forms';

import {
  HexList,
  HexTable,
  HexPage,
} from './Components';
import {
  HexSearchForm,
  HexPaginationForm,
  HexDateForm,
  HexLoginForm,
  HexSignUpForm,
  HexResetForm
} from './Forms';
import { Api } from './Api';
import { login, logout, RootState } from './Store';

export const PATHS = {
  HOME: '/',
  LOGIN: '/login',
  SIGNUP: '/signup',
  RESET: '/reset',
  LOGOUT: '/logout',
  PRODUCTS: '/products',
  USERS: '/users',
  SALES: '/sales',
  PURCHASES: '/purchases',
  MESSAGES: '/messages',
  MEETINGS: '/meetings',
  PTO: '/pto',
  WORK: '/work',
  SCHEDULE: '/schedule',
  USER_DETAILS: '/user/:id',
  PRODUCT_DETAILS: '/product/:id',
  SALE_DETAILS: '/sale/:id',
  PURCHASE_DETAILS: '/purchase/:id',
  MEETING_DETAILS: '/meeting/:id',
  MESSAGE_DETAILS: '/message/:id',
  SCHEDULE_DETAILS: '/schedule/:id',
  WORK_DETAILS: '/work/:id',
  PTO_DETAILS: '/pto/:id',
  USER_EDIT: '/user/:id/edit',
  PRODUCT_EDIT: '/product/:id/edit',
  MEETING_EDIT: '/meeting/:id/edit',
  INVITE: '/meeting/:id/invite',
  ATTACH: '/meeting/:id/attach',
  MESSAGE_EDIT: '/message/:id/edit',
  ADD_RECIPIENT: '/message/:id/add',
  PRODUCT_CREATE: '/product/create',
  SALE_CREATE: '/sale/create',
  PURCHASE_CREATE: '/purchase/create',
  MEETING_CREATE: '/meeting/create',
  MESSAGE_CREATE: '/message/create',
  SCHEDULE_CREATE: '/schedule/create',
  PTO_CREATE: '/pto/create',
}

export const ProductCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Product">
    <HexCreateProductForm
      onSubmit={async ({ name, price }: { name: string, price: number }) => {
        const response: any = await Api.createProduct(name, price);
        if (response?.id)
          navigate(PATHS.PRODUCT_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const SaleCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Sale">
    <HexCreateSaleForm
      onSubmit={async ({ productId, amount, price }: { productId: number, amount: number, price: number }) => {
        const response: any = await Api.createSale(productId, amount, price);
        if (response?.id)
          navigate(PATHS.SALE_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const PurchaseCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Purchase">
    <HexCreatePurchaseForm
      onSubmit={async ({ productId, amount, cost }: { productId: number, amount: number, cost: number }) => {
        const response: any = await Api.createPurchase(productId, amount, cost);
        if (response?.id)
          navigate(PATHS.PURCHASE_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const MeetingCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Meeting">
    <HexCreateMeetingForm
      onSubmit={async ({ title, day }: { title: string, day: Date }) => {
        const response: any = await Api.createMeeting(title, day);
        if (response?.id)
          navigate(PATHS.MEETING_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const MeetingInvitePage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  if (!id) return null;
  return <HexPage title="Invite Participants">
    {id && <HexInviteUserToMeetingForm
      meetingId={parseInt(id)}
      onSubmit={async ({ userId }: { userId: number }) => {
        if (id) {
          const response: any = await Api.addUserToMeeting(parseInt(id), userId);
          if (response)
            navigate(PATHS.MEETING_DETAILS.replace(':id', id));
        }
      }}
    />}
  </HexPage>
}

export const AddRecipientPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  return <HexPage title="Add Recipient">
    {id && <HexInviteUserToMeetingForm
      meetingId={parseInt(id)}
      onSubmit={async ({ userId }: { userId: number }) => {
        if (id) {
          const response: any = await Api.addUserToMessage(parseInt(id), userId);
          if (response)
            navigate(PATHS.MESSAGE_DETAILS.replace(':id', id));
        }
      }}
    />}
  </HexPage>
}

export const MeetingAttachPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  if (!id) return null;
  return <HexPage title="Attach File">
    <HexUploadFileForm
      onSubmit={async ({ file, name }: { file: File, name: string }) => {
        const response: any = await Api.uploadFileToMeeting(parseInt(id), file, name);
        if (response)
          navigate(PATHS.MEETING_DETAILS.replace(':id', id));
      }}
    />
  </HexPage>
}

export const MessageCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Message">
    <HexCreateMessageForm
      onSubmit={async ({ subject, text }: { subject: string, text: string }) => {
        const response: any = await Api.createMessage(text, subject);
        if (response?.id)
          navigate(PATHS.MESSAGE_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const ScheduleCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Schedule">
    <HexCreateScheduleForm
      onSubmit={async ({ userId, dayOfWeek, start, end }: { userId: number, dayOfWeek: string, start: string, end: string }) => {
        const response: any = await Api.createSchedule(userId, start, end, dayOfWeek.toUpperCase());
        if (response?.id)
          navigate(PATHS.SCHEDULE_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const PtoCreatePage = () => {
  const navigate = useNavigate();
  return <HexPage title="Create Time Off">
    <HexCreatePtoForm
      onSubmit={async ({ userId, type, day }: { userId: number, type: string, day: Date }) => {
        const response: any = await Api.createPto(userId, day, type);
        if (response?.id)
          navigate(PATHS.PTO_DETAILS.replace(':id', response.id));
      }}
    />
  </HexPage>
}

export const UserEditPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [user, isLoading] = useLoader(() => id ? Api.getUser(parseInt(id)) : null, [id]);
  if (!user) return null;
  return <HexPage title={`Edit ${user.name}`}>
    <HexUpdateUserForm
      isLoading={isLoading}
      user={user}
      onSubmit={async ({ id, name }: { id: number, name: string }) => {
        await Api.updateUser(id, name);
        navigate(PATHS.USER_DETAILS.replace(':id', id.toString()));
      }}
    />
  </HexPage>
}

export const ProductEditPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [product, isLoading] = useLoader(() => id ? Api.getProduct(parseInt(id)) : null, [id]);
  if (!product) return null;
  return <HexPage title="Edit Product">
    <HexUpdateProductForm
      isLoading={isLoading}
      product={product}
      onSubmit={async ({ id, name, price }: { id: number, name: string, price: number }) => {
        await Api.updateProduct(id, name, price);
        navigate(PATHS.PRODUCT_DETAILS.replace(':id', id.toString()));
      }}
    />
  </HexPage>
}

export const UsersPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [data, isLoading] = useLoader(() => Api.listUsers({page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc, query: pagination.query}), [pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Users Management">
    <HexSearchForm onSubmit={({ term }: { term: string }) => pagination.setQuery(term)}/>
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "name", "role", "isActive", "lastLoginDate", "signUpDate"]}
      onClick={(item: any) => { navigate(PATHS.USER_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'name', 'email', 'role']}
    />
  </HexPage>
}

export const ProductsPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [data, isLoading] = useLoader(() => Api.listProducts({page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc, query: pagination.query}), [pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Products Management">
    <HexProductListForm />
    <HexSearchForm onSubmit={({ term }: { term: string }) => pagination.setQuery(term)}/>
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "name", "price", "isActive"]}
      onClick={(item: any) => { navigate(PATHS.PRODUCT_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'name']}
    />
  </HexPage>
}

export const SalesPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listSales({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Sales Management">
    <HexSaleListForm />
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "productId", "salesmanId", "datetime", "amount", "price"]}
      onClick={(item: any) => { navigate(PATHS.SALE_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'product', 'salesman', 'datetime']}
    />
  </HexPage>
}

export const PurchasesPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listPurchases({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Purchases Management">
    <HexPurchaseListForm />
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "productId", "supplierId", "datetime", "amount", "cost"]}
      onClick={(item: any) => { navigate(PATHS.PURCHASE_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'product', 'provider', 'datetime']}
    />
  </HexPage>
}

export const MessagesPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listMessages({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Messages Management">
    <HexMessageListForm />
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "senderId", "subject", "creationDate", "sentAt"]}
      onClick={(item: any) => { navigate(PATHS.MESSAGE_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'sender', 'subject', 'sentAt']}
    />
  </HexPage>
}

export const MeetingsPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listMeetings({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Meetings Management">
    <HexMeetingListForm />
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "title", "date"]}
      onClick={(item: any) => { navigate(PATHS.MEETING_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'title', 'date']}
    />
  </HexPage>
}

export const PtoPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listPto({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Time Off Management">
    <HexPtoListForm />
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "userId", "type", "day"]}
      onClick={(item: any) => { navigate(PATHS.PTO_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'user', 'day']}
    />
  </HexPage>
}

export const WorkPage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [start, setStart] = useState(getMonthsAgo(6));
  const [end, setEnd] = useState(getMonthsAhead(6));
  const [data, isLoading] = useLoader(() => Api.listWork({start, end, page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [start, end, pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Work Time Management">
    <HexDateForm
      start={start}
      end={end}
      onChange={(start: Date, end: Date) => {
        setStart(start);
        setEnd(end);
      }}
    />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "userId", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.WORK_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'user', 'startTime', 'endTime']}
    />
  </HexPage>
}

export const SchedulePage = () => {
  const navigate = useNavigate();
  const pagination = usePagination();
  const [data, isLoading] = useLoader(() => Api.listSchedule({page: pagination.page, size: pagination.size, sort: pagination.sort, asc: pagination.asc}), [pagination.page, pagination.asc, pagination.sort, pagination.query, pagination.size]);
  return <HexPage title="Schedule Management">
    <HexScheduleListForm />
    <HexList
      data={data}
      isLoading={isLoading}
      columns={["id", "userId", "dayOfWeek", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.SCHEDULE_DETAILS.replace(':id', item.id)); }}
    />
    <HexPaginationForm
      pagination={pagination}
      isLoading={isLoading}
      sortOptions={['id', 'user', 'dayOfWeek']}
    />
  </HexPage>
}

export const LoginPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  return <HexPage title="Login">
    <HexLoginForm onSubmit={async ({email, password}: { email: string, password: string }) => {
      const response: any = await Api.login(email, password)
      if (response?.Token && response?.userId && response?.userName) {
        dispatch(login(response));
        navigate(PATHS.HOME);
      }
    }}/>
  </HexPage>
}

export const SignUpPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  return <HexPage title="Sign Up">
    <HexSignUpForm onSubmit={async ({email, password}: { email: string, password: string }) => {
      const response: any = await Api.signUp(email, password)
      if (response?.Token && response?.userId && response?.userName) {
        dispatch(login(response));
        navigate(PATHS.HOME);
      }
    }}/>
  </HexPage>
}

export const ResetPage = () => {
  const dispatch = useDispatch();
  return <HexPage title="Reset Password">
    <HexResetForm onSubmit={async ({email}: { email: string }) => {
      const response: any = await Api.resetPassword(email)
      if (response?.Token && response?.userId && response?.userName)
          dispatch(login(response));
    }}/>
  </HexPage>
}

export const LogoutPage = () => {
  const dispatch = useDispatch();
  dispatch(logout());
  return null;
}

export const HomePage = () => {
  const session = useSelector((state: RootState) => state.session);
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => Api.getMyProfile(), [id]);
  const [pto, isPtoLoading] = useLoader(() => Api.listMyPto(0, 10, 'day', false), [id]);
  const [sales, isSalesLoading] = useLoader(() => Api.listMySales(0, 10, 'datetime', false), [id]);
  const [purchases, isPurchasesLoading] = useLoader(() => Api.listMyPurchases(0, 10, 'datetime', false), [id]);
  const [work, isWorkLoading, reloadWork] = useLoader(() => Api.listMyWork(0, 10, 'startTime', false), [id]);
  const [schedule, isScheduleLoading] = useLoader(() => Api.listMySchedule(0, 10, 'dayOfWeek', false), [id]);
  const [messages, isMessagesLoading] = useLoader(() => Api.listMyMessages(0, 10, 'sentAt', false), [id]);
  const [meetings, isMeetingsLoading] = useLoader(() => Api.listMyMeetings(0, 10, 'date', false), [id]);
  if (session.id === undefined || session.id === null) return;
  return <HexPage title={`Welcome ${session.name}!`}>
    <HexTable
      title="Your Profile"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`UserDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
      }}
    />
    <HexWorkForm
      title="Check In/Out"
      reload={reloadWork}
      onCheckIn={async () => { await Api.checkIn(); }}
      onCheckOut={async () => { await Api.checkOut(); }}
    />
    <HexList
      title="Your Working Hours"
      isLoading={isWorkLoading}
      data={work}
      columns={["id", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.WORK_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Messages"
      isLoading={isMessagesLoading}
      data={messages}
      columns={["id", "senderId", "subject", "creationDate", "sentAt"]}
      onClick={(item: any) => { navigate(PATHS.MESSAGE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Incoming Meetings"
      isLoading={isMeetingsLoading}
      data={meetings}
      columns={["id", "title", "date"]}
      onClick={(item: any) => { navigate(PATHS.MEETING_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Schedule"
      isLoading={isScheduleLoading}
      data={schedule}
      columns={["id", "dayOfWeek", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.SCHEDULE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Time Off"
      isLoading={isPtoLoading}
      data={pto}
      columns={["id", "type", "day"]}
      onClick={(item: any) => { navigate(PATHS.PTO_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Recent Sales"
      isLoading={isSalesLoading}
      data={sales}
      columns={["id", "productId", "datetime", "amount", "price"]}
      onClick={(item: any) => { navigate(PATHS.SALE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Your Recent Purchases"
      isLoading={isPurchasesLoading}
      data={purchases}
      columns={["id", "productId", "datetime", "amount", "cost"]}
      onClick={(item: any) => { navigate(PATHS.PURCHASE_DETAILS.replace(':id', item.id)); }}
    />
  </HexPage>
}

export const UserDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading, reload] = useLoader(() => id ? Api.getUser(parseInt(id)) : null, [id]);
  const [sales, isSalesLoading] = useLoader(() => id ? Api.listSalesByUser(parseInt(id), 0, 10, 'datetime', false) : null, [id]);
  const [purchases, isPurchasesLoading] = useLoader(() => id ? Api.listPurchasesByUser(parseInt(id), 0, 10, 'datetime', false) : null, [id]);
  const [pto, isPtoLoading] = useLoader(() => id ? Api.listPtoByUser(parseInt(id), 0, 10, 'day', false) : null, [id]);
  const [work, isWorkLoading] = useLoader(() => id ? Api.listWorkByUser(parseInt(id), 0, 10, 'startTime', false) : null, [id]);
  const [schedule, isScheduleLoading] = useLoader(() => id ? Api.listScheduleRulesByUser(parseInt(id), 0, 10, 'dayOfWeek', false) : null, [id]);
  const [messages, isMessagesLoading] = useLoader(() => id ? Api.listMessagesByUser(parseInt(id), 0, 10, 'sentAt', false) : null, [id]);
  const [meetings, isMeetingsLoading] = useLoader(() => id ? Api.listMeetingsByUser(parseInt(id), 0, 10, 'date', false) : null, [id]);
  return <HexPage title={`User #${id} Management`}>
    <HexUserDetailsForm
      user={data}
      reload={reload}
      onActivate={async (id: number) => { await Api.activateUser(id); }}
      onDeactivate={async (id: number) => { await Api.deactivateUser(id); }}
      onPromoteManager={async (id: number) => { await Api.promoteManager(id); }}
      onPromoteSalesman={async (id: number) => { await Api.promoteSalesman(id); }}
      onPromoteProvider={async (id: number) => { await Api.promoteProvider(id); }}
      onDemoteManager={async (id: number) => { await Api.demoteManager(id); }}
      onDemoteSalesman={async (id: number) => { await Api.demoteSalesman(id); }}
      onDemoteProvider={async (id: number) => { await Api.demoteProvider(id); }}
    />
    <HexTable
      title="User Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`UserDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
      }}
    />
    <HexList
      title="Messages"
      isLoading={isMessagesLoading}
      data={messages}
      columns={["id", "senderId", "subject", "creationDate", "sentAt"]}
      onClick={(item: any) => { navigate(PATHS.MESSAGE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Meetings"
      isLoading={isMeetingsLoading}
      data={meetings}
      columns={["id", "title", "date"]}
      onClick={(item: any) => { navigate(PATHS.MEETING_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Schedule"
      isLoading={isScheduleLoading}
      data={schedule}
      columns={["id", "dayOfWeek", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.SCHEDULE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Time Off"
      isLoading={isPtoLoading}
      data={pto}
      columns={["id", "type", "day"]}
      onClick={(item: any) => { navigate(PATHS.PTO_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Work Time"
      isLoading={isWorkLoading}
      data={work}
      columns={["id", "startTime", "endTime"]}
      onClick={(item: any) => { navigate(PATHS.WORK_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Sales"
      isLoading={isSalesLoading}
      data={sales}
      columns={["id", "productId", "datetime", "amount", "price"]}
      onClick={(item: any) => { navigate(PATHS.SALE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Purchases"
      isLoading={isPurchasesLoading}
      data={purchases}
      columns={["id", "productId", "datetime", "amount", "cost"]}
      onClick={(item: any) => { navigate(PATHS.PURCHASE_DETAILS.replace(':id', item.id)); }}
    />
  </HexPage>
}

export const ProductDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading, reload] = useLoader(() => id ? Api.getProduct(parseInt(id)) : null, [id]);
  const [sales, isSalesLoading] = useLoader(() => id ? Api.listSalesByProduct(parseInt(id), 0, 10, 'datetime', false) : null, [id]);
  const [purchases, isPurchasesLoading] = useLoader(() => id ? Api.listPurchasesByProduct(parseInt(id), 0, 10, 'datetime', false) : null, [id]);
  return <HexPage title={`Product #${id} Management`}>
    <HexProductDetailsForm
      product={data}
      reload={reload}
      onDeactivate={async (id: number) => { await Api.deleteProduct(id); }}
    />
    <HexTable
      title="Product Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`ProductDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
      }}
    />
    <HexList
      title="Sales"
      isLoading={isSalesLoading}
      data={sales}
      columns={["id", "salesmanId", "datetime", "amount", "price"]}
      onClick={(item: any) => { navigate(PATHS.SALE_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Purchases"
      isLoading={isPurchasesLoading}
      data={purchases}
      columns={["id", "supplierId", "datetime", "amount", "cost"]}
      onClick={(item: any) => { navigate(PATHS.PURCHASE_DETAILS.replace(':id', item.id)); }}
    />
  </HexPage>
}

export const SaleDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getSale(parseInt(id)) : null, [id]);
  const [user, isUserLoading] = useLoader(() => data && data.salesmanId ? Api.getUser(data.salesmanId) : null, [data])
  const [product, isProductLoading] = useLoader(() => data && data.productId ? Api.getProduct(data.productId) : null, [data])
  return <HexPage title={`Sale #${id} Management`}>
    <HexTable
      title="Sale Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`SaleDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'productId')
          navigate(PATHS.PRODUCT_DETAILS.replace(':id', data.productId));
        if (key == 'salesmanId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.salesmanId));
      }}
    />
    <HexTable
      title="Product Details"
      isLoading={isProductLoading}
      data={product}
      onClick={(key: string, data: any) => {
        console.log(`SaleDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.PRODUCT_DETAILS.replace(':id', data.id));
      }}
    />
    <HexTable
      title="Salesman Details"
      isLoading={isUserLoading}
      data={user}
      onClick={(key: string, data: any) => {
        console.log(`SaleDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.USER_DETAILS.replace(':id', data.id));
      }}
    />
  </HexPage>
}

export const PurchaseDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getPurchase(parseInt(id)) : null, [id]);
  const [user, isUserLoading] = useLoader(() => data && data.supplierId ? Api.getUser(data.supplierId) : null, [data])
  const [product, isProductLoading] = useLoader(() => data && data.productId ? Api.getProduct(data.productId) : null, [data])
  return <HexPage title={`Purchase #${id} Management`}>
    <HexTable
      title="Purchase Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`PurchaseDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'productId')
          navigate(PATHS.PRODUCT_DETAILS.replace(':id', data.productId));
        if (key == 'supplierId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.supplierId));
      }}
    />
    <HexTable
      title="Product Details"
      isLoading={isProductLoading}
      data={product}
      onClick={(key: string, data: any) => {
        console.log(`PurchaseDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.PRODUCT_DETAILS.replace(':id', data.id));
      }}
    />
    <HexTable
      title="Supplier Details"
      isLoading={isUserLoading}
      data={user}
      onClick={(key: string, data: any) => {
        console.log(`PurchaseDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.USER_DETAILS.replace(':id', data.id));
      }}
    />
  </HexPage>
}

export const MeetingDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getMeeting(parseInt(id)) : null, [id]);
  const [invitees, isInviteesLoading] = useLoader(() => id ? Api.listInvitedUsers(parseInt(id), 0, 30, 'name', true) : null, [id])
  const [files, isFilesLoading] = useLoader(() => id ? Api.listMeetingFiles(parseInt(id), 0, 30, 'name', true) : null, [id])
  return <HexPage title={`Meeting #${id} Management`}>
    <HexMeetingDetailsForm
      meeting={data}
      onDelete={async () => {
        await Api.deleteMeeting(parseInt(data.id));
      }}
    />
    <HexTable
      title="Meeting Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`MeetingDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
      }}
    />
    <HexList
      title="Invited Users"
      data={invitees}
      isLoading={isInviteesLoading}
      columns={["id", "name", "role"]}
      onClick={(item: any) => { navigate(PATHS.USER_DETAILS.replace(':id', item.id)); }}
    />
    <HexList
      title="Files"
      data={files}
      isLoading={isFilesLoading}
      columns={["id", "fileName"]}
      onClick={(item: any) => {
        id ? Api.downloadFile(parseInt(id), item.id) : null;
      }}
    />
  </HexPage>
}

export const MessageDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading, reload] = useLoader(() => id ? Api.getMessage(parseInt(id)) : null, [id]);
  const [recipients, isRecipientsLoading] = useLoader(() => id ? Api.listRecipients(parseInt(id), 0, 30, 'name', true) : null, [id])
  return <HexPage title={`Message #${id} Management`}>
    <HexMessageDetailsForm
      message={data}
      onDelete={async () => {
        await Api.deleteMessage(parseInt(data.id));
      }}
      onSend={async () => {
        await Api.sendMessage(parseInt(data.id));
        reload();
      }}
    />
    <HexTable
      title="Message Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`MessageDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'senderId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.senderId));
      }}
    />
    <HexList
      title="Recipients"
      data={recipients}
      isLoading={isRecipientsLoading}
      columns={["id", "name", "role"]}
      onClick={(item: any) => { navigate(PATHS.USER_DETAILS.replace(':id', item.id)); }}
    />
  </HexPage>
}

export const WorkDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getWork(parseInt(id)) : null, [id]);
  const [user, isUserLoading] = useLoader(() => data && data.userId ? Api.getUser(data.userId) : null, [data])
  return <HexPage title={`Work #${id} Management`}>
    <HexTable
      title="Work Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`WorkDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'userId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.userId));
      }}
    />
    <HexTable
      title="User Details"
      isLoading={isUserLoading}
      data={user}
      onClick={(key: string, data: any) => {
        console.log(`WorkDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.USER_DETAILS.replace(':id', data.id));
      }}
    />
  </HexPage>
}

export const PtoDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getPto(parseInt(id)) : null, [id]);
  const [user, isUserLoading] = useLoader(() => data && data.userId ? Api.getUser(data.userId) : null, [data])
  return <HexPage title={`Time Off #${id} Management`}>
    <HexPtoDetailsForm
      pto={data}
      onDelete={async (userId: number, id: number) => {
        await Api.deletePto(userId, id);
      }}
    />
    <HexTable
      title="Time Off Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`PtoDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'userId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.userId));
      }}
    />
    <HexTable
      title="User Details"
      isLoading={isUserLoading}
      data={user}
      onClick={(key: string, data: any) => {
        console.log(`PtoDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.USER_DETAILS.replace(':id', data.id));
      }}
    />
  </HexPage>
}

export const ScheduleDetailsPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [data, isLoading] = useLoader(() => id ? Api.getSchedule(parseInt(id)) : null, [id]);
  const [user, isUserLoading] = useLoader(() => data && data.userId ? Api.getUser(data.userId) : null, [data])
  return <HexPage title={`Schedule #${id} Management`}>
    <HexScheduleDetailsForm
      schedule={data}
      onDelete={async (userId: number, id: number) => {
        await Api.deleteSchedule(userId, id);
      }}
    />
    <HexTable
      title="Schedule Details"
      isLoading={isLoading}
      data={data}
      onClick={(key: string, data: any) => {
        console.log(`ScheduleDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        if (key == 'userId')
          navigate(PATHS.USER_DETAILS.replace(':id', data.userId));
      }}
    />
    <HexTable
      title="User Details"
      isLoading={isUserLoading}
      data={user}
      onClick={(key: string, data: any) => {
        console.log(`ScheduleDetailsPage.onClick: ${key} ${JSON.stringify(data)}`);
        navigate(PATHS.USER_DETAILS.replace(':id', data.id));
      }}
    />
  </HexPage>
}
