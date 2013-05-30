//
//  DBProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-5-30.
//
//

#import "DBProcess.h"
#import "SBJSON.h"
@implementation DBProcess
@synthesize database = _database;
-(id) init{
    self = [super init];
    if(self){
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        self.database = [documentsDirectory stringByAppendingPathComponent:@"uranus"];
    }
    return self;
}

-(void) insertOrUpdateOffileRecord:(OfflineRecord*) offlineRecord{
    NSString* path = [self.database stringByAppendingPathComponent:@"OfflineRecord.json"];
    NSMutableDictionary* records = [NSMutableDictionary dictionary];
    if([[NSFileManager defaultManager] fileExistsAtPath:path]){
        NSString* content = [NSString stringWithContentsOfURL:[NSURL URLWithString:path] encoding:NSUTF8StringEncoding error:nil];
        if(content!=NULL&&content.length>0){
            records = [NSMutableDictionary dictionaryWithDictionary:[content JSONValue]];
        }
    }
    [records setValue:offlineRecord forKey:offlineRecord.url];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:records
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:nil];
    [jsonData writeToFile:path atomically:YES];
}

-(void) deleteOffileRecord:(OfflineRecord*) offlineRecord{
    NSString* path = [self.database stringByAppendingPathComponent:@"OfflineRecord.json"];
    NSMutableDictionary* records = [NSMutableDictionary dictionary];
    if([[NSFileManager defaultManager] fileExistsAtPath:path]){
        NSString* content = [NSString stringWithContentsOfURL:[NSURL URLWithString:path] encoding:NSUTF8StringEncoding error:nil];
        if(content!=NULL&&content.length>0){
            records = [NSMutableDictionary dictionaryWithDictionary:[content JSONValue]];
        }
    }
    [records removeObjectForKey:offlineRecord.url];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:records
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:nil];
    [jsonData writeToFile:path atomically:YES];
}
@end
