//
//  WebContentSync.m
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import "WebContentSync.h"
#import "Constants.h"
#import "ZipArchive.h"
#import "AppDelegate.h"

@implementation WebContentSync
@synthesize progressHUD = _progressHUD;

- (void)setProgress:(float)newProgress{
    self.progressHUD.progress = newProgress;
    self.progressHUD.labelText=[NSString stringWithFormat:@"加载脚本中...%.0f%@",newProgress*100,@"%"];
}
- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes{
    NSLog(@"receiver data %lld",bytes);
}
-(void) syncWebContent:(MBProgressHUD*) progress force:(BOOL) force{
    self.progressHUD = progress;
    ASIHTTPRequest *verreq = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[AppDele.serverIP stringByAppendingFormat:@"%@?t=%f",WEB_CONTENT_SYNC_VERSION_API,[[NSDate new] timeIntervalSince1970]]]];
    if(!force){
        [verreq setTimeOutSeconds:60];
    }
    [verreq setShouldContinueWhenAppEntersBackground:YES];
    [verreq startSynchronous];
    NSError *vererror = [verreq error];
    if(!vererror){
        NSString* version = [[verreq responseHeaders] valueForKey:@"version"];
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        NSString* currentVersion = [defaults objectForKey:@"version"];
        if(force||currentVersion==NULL||![currentVersion isEqualToString:version]){
            NSLog(@"Current version is %@, there is new version %@ to update",currentVersion,version);
            ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:[AppDele.serverIP stringByAppendingFormat:@"%@?t=%f",WEB_CONTENT_SYNC_API,[[NSDate new] timeIntervalSince1970]]]];
            [req setShouldContinueWhenAppEntersBackground:YES];
            [req setDownloadProgressDelegate:self];
            [req setShowAccurateProgress:YES];
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
            }else{
                NSLog(@"Download sync.zip error %@",error.description);
            }
        }else{
            NSLog(@"Current version is %@, there is no need to update new version %@",currentVersion,version);
        }
    }else{
        TTAlert(@"链接服务器超时，无法同步脚本，请进入设置手动同步脚本");
        NSLog(@"Cann't connect to ATV Server by ip:%@",AppDele.serverIP);
    }
}

- (void)dealloc
{
    TT_RELEASE_SAFELY(_progressHUD);
    [super dealloc];
}
@end