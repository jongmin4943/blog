import log4js, { Logger, LoggingEvent } from "log4js";
import chalk from "chalk";
import dayjs from "dayjs";

const createMessageBy = (logEvent: LoggingEvent) =>
	logEvent.data
		.filter((d, i) => i !== 0)
		.map(data =>
			typeof data === "object" ? JSON.stringify(data).replace(/"/g, "") : data,
		)
		.join(" ");

const format = (logEvent: LoggingEvent) => ({
	"@timestamp": logEvent.startTime,
	level: logEvent.level.levelStr,
	message: createMessageBy(logEvent),
	logger_name: logEvent.data[0],
	application_name: "nuxt_app",
});

const prettyPrint = (logEvent: LoggingEvent) =>
	chalk`{gray [${dayjs(logEvent.startTime).format("YYYY-MM-DD hh:mm:ss")}]} {${
		logEvent.level.colour
	} [${logEvent.level.levelStr}] ${JSON.stringify(
		createMessageBy(logEvent),
	)}} - (${logEvent.data[0]})`;

const configLogger = () => {
	const isProd = import.meta.env.PROD;
	const logType = isProd ? "json" : "messagePassThrough";
	const logLevel = isProd ? "info" : "trace";

	log4js.configure({
		appenders: {
			out: { type: "stdout", layout: { type: logType } },
		},
		categories: {
			default: { appenders: ["out"], level: logLevel },
		},
	});

	log4js.addLayout(
		logType,
		() => (logEvent: LoggingEvent) =>
			isProd ? JSON.stringify(format(logEvent)) : prettyPrint(logEvent),
	);
};

const createLogger = (name: string, logger: Logger) => ({
	info: (arg: any, ...args: any) => logger.info(name, arg, ...args),
	warn: (arg: any, ...args: any) => logger.warn(name, arg, ...args),
	trace: (arg: any, ...args: any) => logger.trace(name, arg, ...args),
	debug: (arg: any, ...args: any) => logger.debug(name, arg, ...args),
	error: (arg: any, ...args: any) => logger.error(name, arg, ...args),
});

export default defineNuxtPlugin(() => {
	configLogger();

	return {
		provide: {
			logger: (name?: string) =>
				createLogger(
					name || getCurrentInstance()?.type.__name || "unknown",
					log4js.getLogger(),
				),
		},
	};
});
