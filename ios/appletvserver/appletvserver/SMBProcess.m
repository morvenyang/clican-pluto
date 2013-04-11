//
//  SMBProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-4-10.
//
//

#import "SMBProcess.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
@implementation SMBProcess
@synthesize smbFile = _smbFile;
@synthesize smbPath = _smbPath;

-(void) dealloc{
    TT_RELEASE_SAFELY(_smbPath);
    TT_RELEASE_SAFELY(_smbFile);
    [super dealloc];
}

-(NSString*)getResourcesForParent:(NSString*) parent{
    id result = [KxSMBProvider fetchAtPath:parent];
    NSString* content = nil;
    if ([result isKindOfClass:[NSArray class]]) {
        content=@"[";
        NSArray* array = (NSArray*)result;
        for(int i=0;i<array.count;i++){
            KxSMBItem* item = [array objectAtIndex:i];
            NSString* fileName = item.path.lastPathComponent;
            NSString* url = @"";
            NSString* type = nil;
            if([item isKindOfClass:[KxSMBItemFile class]]){
                if([AtvUtil content:fileName contains:@"mp4"]){
                    url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/proxy/play.mp4?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:item.path]];
                }else if([AtvUtil content:fileName contains:@"mkv"]){
                    url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/mkv/play.m3u8?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:item.path]];
                }
                type = @"file";
            }else if([item isKindOfClass:[KxSMBItemTree class]]){
                url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/smb/resource?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:item.path]];
                type = @"directory";
            }else{
                continue;
            }
            if(i==array.count-1){
                content = [content stringByAppendingFormat:@"{\"url\":\"%@\",\"type\":\"%@\",\"fileName\":\"%@\"}",url,type,fileName];
            }else{
                content = [content stringByAppendingFormat:@"{\"url\":\"%@\",\"type\":\"%@\",\"fileName\":\"%@\"},",url,type,fileName];
            }
        }
        content = [content stringByAppendingString:@"]"];
    } else if ([result isKindOfClass:[KxSMBItem class]]) {
        NSLog(@"error for smb resource:%@",parent);
    } else {
        NSLog(@"error for smb resource:%@",parent);
    }
    return content;
}
@end
