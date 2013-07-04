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
#import "TouchXML.h"
#import "IndexMenu.h"

@implementation WebContentSync
@synthesize progressHUD = _progressHUD;

- (void)setProgress:(float)newProgress{
    self.progressHUD.progress = newProgress;
    self.progressHUD.labelText=[NSString stringWithFormat:@"加载脚本中...%.0f%@",newProgress*100,@"%"];
}
- (void)request:(ASIHTTPRequest *)request didReceiveBytes:(long long)bytes{
    NSLog(@"receiver data %lld",bytes);
}
-(BOOL) syncWebContent:(MBProgressHUD*) progress force:(BOOL) force{
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
        AppDele.clientVersion = [[verreq responseHeaders] valueForKey:@"clientVersion"];
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
                return TRUE;
            }else{
                NSLog(@"Download sync.zip error %@",error.description);
                return FALSE;
            }
        }else{
            NSLog(@"Current version is %@, there is no need to update new version %@",currentVersion,version);
            return TRUE;
        }
    }else{
        NSLog(@"Cann't connect to ATV Server by ip:%@",AppDele.serverIP);
        return FALSE;
    }
}

-(NSArray*) loadLocalXml{
    NSString* localXmlPath = [[AppDele localWebPathPrefix] stringByAppendingString:@"/appletv/local.xml"];
    NSError *theError = NULL;
    CXMLDocument *document =[[CXMLDocument alloc] initWithContentsOfURL:[NSURL fileURLWithPath:localXmlPath] encoding:NSUTF8StringEncoding options:0 error:&theError];
    CXMLElement* rootElement=[document rootElement];
    CXMLElement* bodyElement = [[rootElement elementsForName:@"body"] objectAtIndex:0];
    NSArray* array = [bodyElement nodesForXPath:@"scroller/items/grid/items/moviePoster" error:nil];
    NSMutableArray* result = [NSMutableArray array];
    for(int i=0;i<array.count;i++){
        CXMLNode* node = [array objectAtIndex:i];
        if([node isKindOfClass:[CXMLElement class]]){
            CXMLElement* gp = (CXMLElement*)node;
            NSString* idName = [[gp attributeForName:@"id"] stringValue];
            if([idName rangeOfString:@"native"].location!=NSNotFound){
                if(AppDele.appleApproveCheck&&[idName rangeOfString:@"approve_check"].location==NSNotFound){
                    continue;
                }
                NSString* onPlay = [[gp attributeForName:@"onPlay"] stringValue];
                NSString* title = [[gp nodeForXPath:@"title" error:nil] stringValue];
                IndexMenu* im = [[[IndexMenu alloc] init] autorelease];
                im.title = title;
                im.onPlay = onPlay;
                [result addObject:im];
            }
        }
    }
    return result;
}
- (void)dealloc
{
    TT_RELEASE_SAFELY(_progressHUD);
    [super dealloc];
}
@end
