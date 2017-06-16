# Getting started with Ruby on Bluemix

This guide will take you through the steps to get started with a simple Ruby application and help you:
  * set up a development environment
  * download sample code
  * run locally
  * run on Bluemix, and
  * add a Bluemix database service to the sample

## Prerequisites

You'll need the following:
* [Bluemix account](https://console.ng.bluemix.net/registration/)
* [Cloud Foundry CLI](https://github.com/cloudfoundry/cli#downloads)
* [Git](https://git-scm.com/downloads)
* [Ruby](https://www.ruby-lang.org/en/downloads/)
* [rbenv](https://github.com/rbenv/rbenv#installation)

## 1. Clone the sample app

Now you're ready to start working with the app. Clone the repo and change the directory to where the sample app is located.
```
git clone https://github.com/IBM-Bluemix/get-started-ruby
cd get-started-ruby
```

## 3. Run the app locally

Dependencies:
```
rbenv install 2.3.0
rbenv local 2.3.0
gem install bundler
bundle install
```

Run the app:
```
rails server
```

View your app at: http://localhost:3000

## 3. Prepare the app for deployment

To deploy to Bluemix, it can be helpful to set up a manifest.yml file. One is provided for you with the sample. Take a moment to look at it.

The manifest.yml includes basic information about your app, such as the name, how much memory to allocate for each instance and the route. In this manifest.yml **random-route: true** generates a random route for your app to prevent your route from colliding with others.  You can replace **random-route: true** with **host: myChosenHostName**, supplying a host name of your choice. [Learn more...](https://console.bluemix.net/docs/manageapps/depapps.html#appmanifest)
 ```
 applications:
 - name: GetStartedRuby
   random-route: true
   memory: 128M
 ```

## 4. Deploy the app

You can use the Cloud Foundry CLI to deploy apps.

Choose your API endpoint
```
cf api <API-endpoint>
```


Replace the *API-endpoint* in the command with an API endpoint from the following list.

|URL                             |Region          |
|:-------------------------------|:---------------|
| https://api.ng.bluemix.net     | US South       |
| https://api.eu-gb.bluemix.net  | United Kingdom |
| https://api.au-syd.bluemix.net | Sydney         |

Login to your Bluemix account

```
cf login
```

From within the *get-started-node* directory push your app to Bluemix
```
cf push
```

This can take a minute. If there is an error in the deployment process you can use the command `cf logs <Your-App-Name> --recent` to troubleshoot.

When deployment completes you should see a message indicating that your app is running.  View your app at the URL listed in the output of the push command.  You can also issue the
```
cf apps
```
command to view your apps status and see the URL.

## 5. Add a database

Next, we'll add a NoSQL database to this application and set up the application so that it can run locally and on Bluemix.

1. Log in to Bluemix in your Browser. Browse to the `Dashboard`. Select your application by clicking on its name in the `Name` column.
2. Click on `Connections` then `Connect new`.
3. In the `Data & Analytics` section, select `Cloudant NoSQL DB` and `Create` the service.
4. Select `Restage` when prompted. Bluemix will restart your application and provide the database credentials to your application using the `VCAP_SERVICES` environment variable. This environment variable is only available to the application when it is running on Bluemix.

Environment variables enable you to separate deployment settings from your source code. For example, instead of hardcoding a database password, you can store this in an environment variable which you reference in your source code. [Learn more...](/docs/manageapps/depapps.html#app_env)

## 6. Use the database

We're now going to update your local code to point to this database. We'll create a json file that will store the credentials for the services the application will use. This file will get used ONLY when the application is running locally. When running in Bluemix, the credentials will be read from the VCAP_SERVICES environment variable.

1. Create a file called `.env` in the `get-started-ruby` directory with the following content:
```
CLOUDANT_URL=
```

2. Back in the Bluemix UI, select your App -> Connections -> Cloudant -> View Credentials

3. Copy and paste just the `url` from the credentials to the `CLOUDANT_URL` field of the `.env` file and save the changes.  The result will be something like:
```
CLOUDANT_URL=https://123456789 ... bluemix.cloudant.com
```

4. Run your application locally.
```
rails server
```

View your app at: http://localhost:3000. Any names you enter into the app will now get added to the database.

Your local app and the Bluemix app are sharing the database.  View your Bluemix app at the URL listed in the output of the push command from above.  Names you add from either app should appear in both when you refresh the browsers.
