//
//  SubTitleProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-5-2.
//
//

#import "SubTitleProcess.h"
#import "ASIHTTPRequest.h"
#import "ASIFormDataRequest.h"
#import "AtvUtil.h"
#import "DataInputStream.h"
#import "AppDelegate.h"
@implementation SubTitleProcess

-(NSString*) downloadSubTitleByFile:(NSString*) fileUrl{
    long totalLength=0;
    KxSMBItemFile* smbFile = nil;
    if([AtvUtil content:fileUrl startWith:@"http"]){
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:fileUrl]];
        NSMutableDictionary* reqHeaders = [NSMutableDictionary dictionary];
        [reqHeaders setValue:[@"bytes=0-" stringByAppendingFormat:@"%i",1] forKey:@"Range"];
        [req setRequestHeaders:reqHeaders];
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req startSynchronous];
        
        NSString* contentRange = [[req responseHeaders] valueForKey:@"Content-Range"];
        if(contentRange!=nil&&[contentRange rangeOfString:@"bytes"].location!=NSNotFound){
            NSMutableCharacterSet* set = [NSMutableCharacterSet new];
            [set addCharactersInString:@"-"];
            [set addCharactersInString:@"/"];
            NSArray* crs = [[contentRange stringByReplacingOccurrencesOfString:@"bytes " withString:@""] componentsSeparatedByCharactersInSet: set];
            totalLength = [(NSString*)[crs objectAtIndex:2] longLongValue];
        }else{
            NSString* contentLength=[[req responseHeaders] valueForKey:@"Content-Length"];
            if(contentLength!=nil&&[contentLength length]>0){
                totalLength = [contentLength longLongValue];
            }
        }
    }else{
        smbFile = [KxSMBProvider fetchAtPath:fileUrl];
        totalLength = smbFile.stat.size;
    }
    if(smbFile){
        [smbFile close];
    }
    [NSString stringWithFormat:@"%ld",totalLength / 3 * 2];
    NSInteger offsets[] = {4096, totalLength / 3 * 2,
        totalLength / 3,totalLength - 8192};
    NSString* hashes = @"";
    for(int i=0;i<4;i++){
        long o = offsets[i];
        NSData* data = nil;
        if([AtvUtil content:fileUrl startWith:@"http"]){
            NSString* range = [NSString stringWithFormat:@"bytes=%ld-%ld",o,o+4096-1];
            NSMutableDictionary* headers = [NSMutableDictionary dictionary];
            [headers setObject:range forKey:@"Range"];
            ASIHTTPRequest *r = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:fileUrl]];
            [r setRequestHeaders:headers];
            [r setShouldContinueWhenAppEntersBackground:YES];
            [r startSynchronous];
            data = [r responseData];
        }else{
            [smbFile seekToFileOffset:o whence:SEEK_SET];
            data = [smbFile readDataOfLength:4096];
           
        }
        NSString* md5 = [AtvUtil md5:data];
        hashes= [hashes stringByAppendingFormat:@"%@;",md5];
    }
//    if(smbFile){
//         [smbFile close];
//    }
    hashes = [hashes substringToIndex:[hashes length]-1];
    NSLog(@"hashes=%@",hashes);
    NSString* shortName = @"1";
    NSString* pathInfo = @"E:\\1.mkv";
    NSData* d1 = [@"SP,aerSP,aer 1543 &e(" dataUsingEncoding:NSUTF8StringEncoding];
    Byte d2[] = {0xd7,0x02};
    NSData* d3 = [[@" 1.mkv" stringByAppendingFormat:@" %@",hashes] dataUsingEncoding:NSUTF8StringEncoding];
    NSMutableData* d =[NSMutableData data];
    [d appendData:d1];
    [d appendBytes:d2 length:2];
    [d appendData:d3];
    NSString* vhash = [AtvUtil md5:d];
    
    
    ASIFormDataRequest *subTitleReq = [ASIFormDataRequest requestWithURL:[NSURL URLWithString:@"http://svplayer.shooter.cn/api/subapi.php"]];
    NSMutableDictionary* subTitleReqHeaders = [NSMutableDictionary dictionary];
    [subTitleReqHeaders setObject:@"SPlayer Build 1543" forKey:@"User-Agent"];
    [subTitleReq setPostValue:hashes forKey:@"filehash"];
    [subTitleReq setPostValue:pathInfo forKey:@"pathinfo"];
    
    [subTitleReq setPostValue:shortName forKey:@"shortname"];
    [subTitleReq setPostValue:vhash forKey:@"vhash"];
    [subTitleReq setRequestHeaders:subTitleReqHeaders];
    [subTitleReq setShouldContinueWhenAppEntersBackground:YES];
    [subTitleReq startSynchronous];
    NSData* data = [subTitleReq responseData];
    if([data length]<1024){
        return nil;
    }else{
        DataInputStream* dis = [DataInputStream dataInputStreamWithData:data];
        return [self parsePackages:dis];
    }
}

-(NSString*) parsePackages:(DataInputStream*) dis{
    int packageCount = [dis readByte];
    for (int i = 0; i < packageCount; i++) {
        int packageLength = [dis readInt];
        int descLength = [dis readInt];
        NSString* desc = [dis readString:descLength dataUsingEncoding:CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)];
        NSLog(@"package desc:%@",desc);
        int fileDataLength = [dis readInt];
        int fileCount = [dis readByte];
        for (int j = 0; j < fileCount; j++) {
            NSString* fileContent = [self parseFile:dis];
            if (fileContent!=nil&&fileContent.length>0) {
                return fileContent;
            }
        }
    }
    return nil;
}

-(NSString*) parseFile:(DataInputStream*) dis{
    int filePackageLength = [dis readInt];
    int fileExtNameLength = [dis readInt];
    NSString* fileExtName = [dis readString:fileExtNameLength dataUsingEncoding:CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)];
    int fileDataLength = [dis readInt];
    NSData* fileData = [dis readData:fileDataLength];
    NSString* fileContent = nil;
    Byte *fds = (Byte*)fileData.bytes;
    if (fds[0] == 0x1f && fds[1] == 0x8b) {
        // gzip
        fileData = [AtvUtil uncompressZippedData:fileData];
    }
    fds = (Byte*)fileData.bytes;
    if (fds[0] ==  0xff && fds[1] == 0xfe) {
        fileContent = [[[NSString alloc] initWithData:fileData encoding:NSUTF16StringEncoding] autorelease];
    } else {
        fileContent = [[[NSString alloc] initWithData:fileData encoding:CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)] autorelease];
    }
    NSLog(@"%@",fileContent);
    return fileContent;
}
@end
