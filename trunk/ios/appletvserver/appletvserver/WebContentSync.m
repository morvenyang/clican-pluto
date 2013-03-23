//
//  WebContentSync.m
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import "WebContentSync.h"
#import "Constants.h"
#import "ASIHTTPRequest.h"
#import "ZipArchive.h"
#import "AppDelegate.h"

@implementation WebContentSync

-(void) syncWebContent{
    
    ASIHTTPRequest *verreq = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[ATV_SERVER_IP stringByAppendingFormat:@"%@?t=%f",WEB_CONTENT_SYNC_VERSION_API,[[NSDate new] timeIntervalSince1970]]]];
    [verreq setShouldContinueWhenAppEntersBackground:YES];
    [verreq startSynchronous];
     NSString* version = [[verreq responseHeaders] valueForKey:@"version"];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* currentVersion = [defaults objectForKey:@"version"];
    if(currentVersion==NULL||![currentVersion isEqualToString:version]){
         NSLog(@"Current version is %@, there is new version %@ to update",currentVersion,version);
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[ATV_SERVER_IP stringByAppendingFormat:@"%@?t=%f",WEB_CONTENT_SYNC_API,[[NSDate new] timeIntervalSince1970]]]];
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req startSynchronous];
        NSError *error = [req error];
        if (!error) {
            NSData* contentData = [req responseData];
            NSString* filePath = [NSTemporaryDirectory() stringByAppendingString:@"/sync.zip"];
            [[NSFileManager defaultManager] createFileAtPath:filePath contents:contentData attributes:nil];
            ZipArchive *zipArchive = [[ZipArchive alloc] init];
            [zipArchive UnzipOpenFile:filePath];
            
            NSString* appletvPath = [[AppDele localWebPathPrefix] stringByAppendingString:@"/appletv"];
            [[NSFileManager defaultManager] removeItemAtPath:appletvPath error:nil];
            [[NSFileManager defaultManager] createDirectoryAtPath:appletvPath withIntermediateDirectories:YES attributes:nil error:nil];
            [zipArchive UnzipFileTo:appletvPath overWrite:YES];
            [zipArchive UnzipCloseFile];
            [defaults setValue:version forKey:@"version"];
        }
    }else{
        NSLog(@"Current version is %@, there is no need to update new version %@",currentVersion,version);
    }
}
@end
