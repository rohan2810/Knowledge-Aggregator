CREATE TABLE public."Knowledge" (
	"Knowledge_ID" character varying NOT NULL,
	"Knowledge_Type" character varying,
 	"Knowledge_Status" character varying,
    "Knowledge_Data" character varying,
    "Knowledge_Acceptance" character varying,
    "Knowledge_Rating" character varying,
    "Object_ID" integer,
    "User_ID" character varying,
 	"State_ID" character varying,
    PRIMARY KEY ("Knowledge_ID")
	ADD CONSTRAINT "Object_ID" FOREIGN KEY ("Object_ID") REFERENCES public."Object"("Object_ID"),
    ADD CONSTRAINT "State_ID" FOREIGN KEY ("State_ID") REFERENCES public."Object_State"("State_ID"),
    ADD CONSTRAINT "User_ID" FOREIGN KEY ("User_ID") REFERENCES public."User"("User_ID") 
	);
    


CREATE TABLE public."Object"(
   	"Object_ID" integer NOT NULL,
    "Object_Name" character varying NOT NULL,
   	"Object_Type" character varying,
    "Object_Description" character varying,
   	"User_ID" character varying,
	PRIMARY KEY ("Object_ID"),
   	ADD CONSTRAINT "User_ID" FOREIGN KEY ("User_ID") REFERENCES public."User"("User_ID") 
);

CREATE TABLE public."Object_State"(
	"State_ID" character varying NOT NULL,
    "Object_Status" character varying,
    "Object_Signature" character varying,
    "Object_ID" integer,
    PRIMARY KEY ("State_ID")
 	ADD CONSTRAINT "Object_ID" FOREIGN KEY ("Object_ID") REFERENCES public."Object"("Object_ID")
);

CREATE TABLE public."User" (
     "User_ID" character varying NOT NULL,
     "User_Role" character varying,
     "User_Details" character varying,
     PRIMARY KEY ("User_ID")
 );
 